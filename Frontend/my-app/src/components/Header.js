// src/components/Header.js
import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import './Header.css'; // Import the main Header CSS
import ProfileDropdown from './ProfileDropdown';
import NotificationDropdown from './NotificationDropdown';
import Search from './Search';

function Header({ onSearch }) {
  const navigate = useNavigate();
  const location = useLocation();
  const isInstructorDashboard = location.pathname.startsWith('/instructor');

  return (
    <header className="header-container bg-light py-2">
      <div className="container-fluid d-flex align-items-center">
        {/* App Name */}
        <h1 className="mb-0 app-name" style={{ color: 'blue' }}>Hive5Skills</h1>

        {/* Search Bar */}
        <Search onSearch={onSearch} />

        {/* Navigation Icons */}
        <div className="d-flex align-items-center">
          <Link to={isInstructorDashboard ? "/instructor" : "/student"} className="nav-link px-2">
            <FontAwesomeIcon icon={faHome} size="lg" />
          </Link>

          {!isInstructorDashboard && <NotificationDropdown />}

          <ProfileDropdown />
        </div>
      </div>
    </header>
  );
}

export default Header;