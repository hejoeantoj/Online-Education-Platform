import React from 'react';
import { useNavigate } from 'react-router-dom';
import './LandingPage.css';

const LandingPage = () => {
  const navigate = useNavigate();

  return (
    <div className="landing-page">
      <div className="overlay">
        <button className="btn login" onClick={() => navigate('/login')}>Login</button>
        <button className="btn register" onClick={() => navigate('/register')}>Register</button>
      </div>
    </div>
  );
};

export default LandingPage;
