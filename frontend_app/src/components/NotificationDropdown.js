// src/components/NotificationDropdown.js
import React, { useState, useEffect } from 'react';
import { fetchNotifications } from './NotificationService'; // Import your service
import './Header.css';

function NotificationDropdown() {
  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const [hasNewNotifications, setHasNewNotifications] = useState(false);
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const checkNewNotifications = async () => {
      const data = await fetchNotifications();
      if (data && Array.isArray(data) && data.length > 0) {
        setHasNewNotifications(true);
        setNotifications(data);
      } else {
        setHasNewNotifications(false);
        setNotifications([]);
      }
    };

    checkNewNotifications();
  }, []); // Removed isInstructorDashboard dependency

  useEffect(() => {
    if (isNotificationsOpen) {
      loadLatestNotifications();
    }
  }, [isNotificationsOpen]);

  const toggleNotifications = async () => {
    setIsNotificationsOpen(!isNotificationsOpen);
    if (!isNotificationsOpen) {
      setHasNewNotifications(false);
      await loadLatestNotifications();
    }
  };

  const loadLatestNotifications = async () => {
    const data = await fetchNotifications();
    if (data && Array.isArray(data)) {
      const latestNotifications = data
        .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
        .slice(0, 3);
      setNotifications(latestNotifications);
    } else {
      setNotifications([]);
    }
  };

  return (
    <div className="notification-dropdown">
      <button
        onClick={toggleNotifications}
        className="notification-button nav-link px-2"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="#000000"
          style={{ verticalAlign: 'middle' }}
        >
          <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 4.36 6 6.93 6 10v5l-2 2v1h16v-1l-2-2z" />
        </svg>
        {hasNewNotifications && (
          <span className="notification-badge">!</span>
        )}
      </button>
      {isNotificationsOpen && (
        <div className="notifications-panel">
          <h4>Notifications</h4>
          {notifications.length > 0 ? (
            notifications.map((notification) => (
              <div key={notification.notificationId} className="notification-item">
                {notification.message}
              </div>
            ))
          ) : (
            <div className="no-notifications">No new notifications</div>
          )}
        </div>
      )}
    </div>
  );
}

export default NotificationDropdown;