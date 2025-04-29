// src/components/Chat/MessageList.jsx
import React, { useEffect, useRef } from 'react';
import MessageItem from './MessageItem';
import styles from './MessageList.module.css';

const MessageList = ({ messages, usernames, currentUserId }) => {
  const messagesEndRef = useRef(null);

  // Function to scroll to the bottom
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  // Scroll to bottom when messages change
  // useEffect(() => {
  //   scrollToBottom();
  // }, [messages]); // Dependency array includes messages

  return (
    <div className={styles.messageListContainer}>
      {messages.map((msg) => (
        <MessageItem
          key={msg.chatId} // Use chatId as the key
          message={msg}
          username={usernames[msg.userId]}
          currentUserId={currentUserId}
        />
      ))}
      {/* Empty div to help scrolling to the bottom */}
      <div ref={messagesEndRef} />
    </div>
  );
};

export default MessageList;