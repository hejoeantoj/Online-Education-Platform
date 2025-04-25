// src/components/Chat/ChatBox.jsx
import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from "react-router-dom";
import axios from 'axios';
import MessageList from './MessageList';
import MessageInput from './MessageInput';
import styles from './ChatBox.module.css';

const ChatBox = () => {
  const { courseId } = useParams(); 
  const[course,setCourse]=useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [usernames, setUsernames] = useState({});
  const [currentUserId, setCurrentUserId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [isSending, setIsSending] = useState(false);

  
  useEffect(() => {
  
    try {
      const email = localStorage.getItem("email");
      if (email) {
        const userId = localStorage.getItem(`${email}_uuid`);
        if (userId) {
          setCurrentUserId(userId);
        } else {
          console.error("User UUID not found in localStorage for email:", email);
          setError("Could not identify current user.");
        }
      } else {
        console.error("Email not found in localStorage.");
        setError("User email not found. Please log in again.");
      }
    } catch (e) {
      console.error("Error reading from localStorage:", e);
      setError("Error accessing user data.");
    }
  }, []); // Runs only once on mount


  
  useEffect(() => {
    const fetchCourseDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:8082/api/course/courseDetails?courseId=${courseId}`);
        setCourse(response.data.data || null);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching course details:", error);
        setError("Failed to load course details.");
        setLoading(false);
      }
    };
    fetchCourseDetails();
  }, [courseId]);

  // --- Fetch Usernames ---
  const fetchUsernames = useCallback(async (userIds) => {
    // Avoid fetching if list is empty or already fetched
    const uniqueIds = [...new Set(userIds)].filter(id => !usernames[id]);
    if (uniqueIds.length === 0) return;

    try {
      // Use the User Module endpoint
      const response = await axios.post('http://localhost:8070/api/user/fetchUsernamesByIds', uniqueIds);
      if (response.data && typeof response.data.data === 'object') {
         // Assuming response.data.data is an object like { userId1: "Name1", userId2: "Name2" }
         setUsernames(prevUsernames => ({ ...prevUsernames, ...response.data.data }));
      } else {
          console.warn("Unexpected format for usernames response:", response.data);
      }
    } catch (error) {
      console.error('Error fetching usernames:', error);
      // Don't block chat functionality, just log the error
    }
  }, [usernames]); // Depend on usernames state to avoid refetching known users

  // --- Fetch Chat Messages ---
  const fetchMessages = useCallback(async () => {
    if (!courseId) return;
    setLoading(true);
    setError(null);
    try {
      // Use the Communication Module endpoint
      const response = await axios.get(`http://localhost:8085/api/forum/chat?courseId=${courseId}`);
      if (response.data && response.data.success && Array.isArray(response.data.data)) {
        const fetchedMessages = response.data.data;
        // Sort messages by timestamp just in case they aren't ordered
        fetchedMessages.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
        setMessages(fetchedMessages);
        // Extract unique user IDs from messages to fetch names
        const userIds = fetchedMessages.map(msg => msg.userId);
        if (userIds.length > 0) {
          fetchUsernames(userIds);
        }
      } else {
        setError(response.data?.message || 'Failed to fetch messages.');
      }
    } catch (err) {
      console.error('Error fetching chat messages:', err);
      setError(`Failed to load chat. ${err.response?.data?.message || err.message}`);
    } finally {
      setLoading(false);
    }
  }, [courseId, fetchUsernames]); // Depend on courseId and fetchUsernames

  // Initial fetch on component mount or when courseId changes
  useEffect(() => {
    fetchMessages();
  }, [fetchMessages]); // Depend on the memoized fetchMessages function

  // --- Send Message Handler ---
  const handleSendMessage = async () => {
    if (!newMessage.trim() || !currentUserId || !courseId) {
      console.warn("Cannot send message: Missing data", { newMessage, currentUserId, courseId });
      return;
    }
    setIsSending(true);
    setError(null); // Clear previous errors

    const messageData = {
      userId: currentUserId,
      courseId: courseId,
      message: newMessage.trim()
    };

    // Optimistic UI update (optional but good UX)
    const optimisticMessage = {
        ...messageData,
        chatId: `temp-${Date.now()}`, // Temporary ID
        timestamp: new Date().toISOString()
    };
    setMessages(prev => [...prev, optimisticMessage]);
    setNewMessage(''); // Clear input immediately

    try {
        // Call the Communication Module endpoint
      const response = await axios.post('http://localhost:8085/api/forum/send', messageData);
      if (response.data && response.data.success && response.data.data) {
        const confirmedMessage = response.data.data;
        // Replace optimistic message with confirmed one
        setMessages(prev => prev.map(msg =>
          msg.chatId === optimisticMessage.chatId ? confirmedMessage : msg
        ));
        // Fetch username if it's a new user (though unlikely for current user)
        fetchUsernames([confirmedMessage.userId]);
      } else {
          throw new Error(response.data?.message || 'Failed to send message.');
      }
    } catch (err) {
      console.error('Error sending message:', err);
      setError(`Failed to send message. ${err.response?.data?.message || err.message}`);
      // Revert optimistic update on error
      setMessages(prev => prev.filter(msg => msg.chatId !== optimisticMessage.chatId));
      setNewMessage(messageData.message); // Restore input content
    } finally {
      setIsSending(false);
    }
  };

  // --- Render Logic ---
  if (!currentUserId && !error) {
      // Still trying to get user ID, show minimal loading or null
      return <div>Loading user...</div>;
  }

  return (
    <div className={styles.chatBoxContainer}>
      {/* <h2 className={styles.chatHeader}>Course Chat: {course?.courseTitle}</h2> */}
      <h2 className={styles.chatHeader}>Discussion Room</h2>

      <button onClick={fetchMessages} disabled={loading} className={styles.refreshButton}>
        {loading ? 'Loading...' : 'Refresh Chat'}
      </button>

      {loading && messages.length === 0 && <p className={styles.statusMessage}>Loading messages...</p>}
      {error && <p className={styles.errorMessage}>Error: {error}</p>}

      {/* Message List Container */}
      <div className={styles.messageListContainer}>
        <MessageList
          messages={messages}
          usernames={usernames}
          currentUserId={currentUserId}
        />
      </div>

      {/* Message Input */}
      <MessageInput
        newMessage={newMessage}
        setNewMessage={setNewMessage}
        handleSendMessage={handleSendMessage}
        isSending={isSending}
      />
    </div>
  );
};

export default ChatBox;