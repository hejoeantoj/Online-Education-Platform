// NotificationService.js
import axios from 'axios';

export const fetchNotifications = async () => {
  try {
    const response = await axios.get('http://localhost:8085/api/notification/view');
    return response.data;
  } catch (error) {
    console.error('Error fetching notifications:', error);
    return [];
  }
};
