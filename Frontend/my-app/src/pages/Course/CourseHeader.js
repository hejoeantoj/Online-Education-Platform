// CourseHeader.js
import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import styles from './CourseHeader.module.css'; // Create a CSS module for the header
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome, faChevronLeft } from '@fortawesome/free-solid-svg-icons';

const CourseHeader = ({ title, onBack, onHome }) => {
  const navigate = useNavigate();

  const handleBack = () => {
    if (onBack) {
      onBack();
    } else {
      navigate(-1); // Default back navigation
    }
  };

  const handleHome = () => {
    if (onHome) {
      onHome();
    } else {
      // Determine the default home route based on user role
      const role = localStorage.getItem('role');
      if (role === 'instructor') {
        navigate('/instructor');
      } else if (role === 'student') {
        navigate('/student');
      } else {
        navigate('/'); // Default fallback
      }
    }
  };

  return (
    <div className={styles.header}>
      <button onClick={handleBack} className={styles.backButton}>
        <FontAwesomeIcon icon={faChevronLeft} />
      </button>
      <h2 className={styles.title}>{title}</h2>
      {/* <button onClick={handleHome} className={styles.homeButton}>
        <FontAwesomeIcon icon={faHome} />
      </button> */}
    </div>
  );
};

export default CourseHeader;