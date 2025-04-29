// src/components/ProfileDropdown.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import profileImage from './profile-photo.png'; // Adjust path
import './Header.css';

function ProfileDropdown() {
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const [userDetails, setUserDetails] = useState(null);
  const navigate = useNavigate();
  const email = localStorage.getItem('email');
  const userId = localStorage.getItem(`${email}_uuid`);
  const userDetailsEndpoint = `http://localhost:8070/api/user/userDetails?userId=${userId}`;

  const userRoleForProfile = {
    "STUDENT": "Student"
  };

  useEffect(() => {
    if (isProfileOpen) {
      fetchUserDetails();
    }
  }, [isProfileOpen]);

  const fetchUserDetails = async () => {
    try {
      const response = await axios.get(userDetailsEndpoint);
      if (response.data.success) {
        setUserDetails(response.data.data);
      } else {
        console.error('Error fetching user details:', response.data.errorMessage);
      }
    } catch (error) {
      console.error('Could not fetch user details:', error);
    }
  };

  const toggleProfile = () => {
    setIsProfileOpen(!isProfileOpen);
  };

  const handleLogout = () => {
    console.log('Logout clicked');
    localStorage.clear();
    navigate('/login');
  };

  return (
    <div className="profile-dropdown">
      <button
        className="profile-button nav-link px-2"
        onClick={toggleProfile}
      >
        <img
          src={profileImage}
          alt="Profile"
          className="profile-image rounded-circle"
        />
        <span className="profile-text">Profile</span>
      </button>
      {isProfileOpen && (
        <div className="profile-menu">
          {userDetails && (
            <div className="user-info">
              <p><i className="bi bi-person-fill"></i> <span>Username:</span> {userDetails.userName}</p>
              <p><i className="bi bi-envelope-fill"></i> <span>Email:</span> {userDetails.email}</p>
              <p><i className="bi bi-tag-fill"></i> <span>Role:</span> {userDetails.role} </p>
            </div>
          )}
          <button className="logout-button" onClick={handleLogout}>
            <i className="bi bi-box-arrow-right"></i> Logout
          </button>
        </div>
      )}
    </div>
  );
}

export default ProfileDropdown;