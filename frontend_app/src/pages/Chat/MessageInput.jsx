// src/components/Chat/MessageInput.jsx
import React from 'react';
import styles from './MessageInput.module.css';

const MessageInput = ({ newMessage, setNewMessage, handleSendMessage, isSending }) => {

  const handleInputChange = (e) => {
    setNewMessage(e.target.value);
  };

  const handleKeyDown = (e) => {
    // Send message on Enter key press (optional)
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault(); // Prevent new line on Enter
      if (newMessage.trim()) { // Check if message is not just whitespace
        handleSendMessage();
      }
    }
  };

  const handleSendClick = () => {
      if (newMessage.trim()) { // Check if message is not just whitespace
          handleSendMessage();
      }
  };

  return (
    <div className={styles.inputContainer}>
      <textarea
        className={styles.inputField}
        value={newMessage}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown} // Add keydown listener
        placeholder="Type your message..."
        rows="2" // Start with 2 rows, can expand
      />
      <button
        className={styles.sendButton}
        onClick={handleSendClick}
        disabled={isSending || !newMessage.trim()} // Disable if sending or input is empty/whitespace
      >
        {isSending ? 'Sending...' : 'Send'}
      </button>
    </div>
  );
};

export default MessageInput;