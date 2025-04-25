import React from 'react';
 import MessageList from '../pages/Chat/MessageList';
 import MessageInput from '../pages/Chat/MessageInput';
 import chatStyles from '../pages/Chat/ChatBox.module.css';

 const QnAView = ({ chatMessages, usernames, studentId, chatLoading, chatError, fetchMessages, newMessage, setNewMessage, handleSendMessage, isSending }) => {
     return (
         <div className={chatStyles.chatBoxContainer}>
             <h3 className={chatStyles.chatHeader}>Course Q&A / Chat</h3>
             <button onClick={() => fetchMessages(true)} disabled={chatLoading || isSending} className={chatStyles.refreshButton}>
                 {chatLoading ? 'Loading...' : 'Refresh Chat'}
             </button>
             {chatLoading && chatMessages.length === 0 && <p className={chatStyles.statusMessage}>Loading messages...</p>}
             {chatError && <p className={chatStyles.errorMessage}>Error: {chatError}</p>}
             <div className={chatStyles.messageListContainer}>
                 <MessageList messages={chatMessages} usernames={usernames} currentUserId={studentId} />
             </div>
             <MessageInput newMessage={newMessage} setNewMessage={setNewMessage} handleSendMessage={handleSendMessage} isSending={isSending} />
         </div>
     );
 };

 export default QnAView;