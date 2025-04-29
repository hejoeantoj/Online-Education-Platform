// src/components/Chat/MessageItem.jsx
import React from 'react';
import styles from './MessageItem.module.css';

const MessageItem = ({ message, username, currentUserId }) => {
  // Determine if the message is from the current logged-in user
  const isCurrentUser = message.userId === currentUserId;

  // Choose the CSS class based on who sent the message
  const messageClass = isCurrentUser ? styles.currentUserMessage : styles.otherUserMessage;

  // Format timestamp (basic example)
  const formatTimestamp = (timestamp) => {
    try {
      return new Date(timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    } catch (e) {
      return 'Invalid date';
    }
  };

  return (
    <div className={`${styles.messageRow} ${isCurrentUser ? styles.currentUserRow : styles.otherUserRow}`}>
      <div className={`${styles.messageBubble} ${messageClass}`}>
        {!isCurrentUser && (
          <div className={styles.senderName}>{username || 'Unknown User'}</div>
        )}
        <div className={styles.messageContent}>{message.message}</div>
        <div className={styles.timestamp}>{formatTimestamp(message.timestamp)}</div>
      </div>
    </div>
  );
};

export default MessageItem;