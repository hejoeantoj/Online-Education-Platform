// NotificationService.js
import axios from 'axios';
 
export const fetchNotifications = async () => {
  try {
    // Get the token from local storage
    const email = localStorage.getItem('email');
    const token = localStorage.getItem(`${email}_token`);
 
    // Create the authorization header
    const config = {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
 
    // Make the GET request with the authorization header
    const response = await axios.get('http://localhost:8085/api/notification/view', config);
    return response.data;
  } catch (error) {
    console.error('Error fetching notifications:', error);
    return [];
  }
};
 
 