// Register.js
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Auth.css'; 

const Register = () => {
  const [userName, setUserName] = useState('');
  const [useremail, setEmail] = useState('');
  const [userpassword, setPassword] = useState('');
  const [userrole, setRole] = useState('STUDENT');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const validateUserName = (name) => {
    if (!name) {
      return 'User name is required.';
    }
    if (!/^[a-zA-Z]/.test(name)) {
      return 'User name must start with an alphabet.';
    }
    if (name.length < 4) {
      return 'User name must be at least 4 characters long.';
    }
    if (!/[a-zA-Z]/.test(name)) {
      return 'User name must contain at least one alphabet.';
    }
    return '';
  };

  const validateEmail = (email) => {
    if (!email || typeof email !== 'string') {
        return 'Email is required.';
    }
 
    email = email.trim();
 
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
        return 'Please enter a valid email address.';
    }
 
    const validDomains = ['gmail.com', 'yahoo.com', 'outlook.com'];
    const domain = email.split('@')[1];
    if (!validDomains.includes(domain)) {
        return `Email domain '${domain}' is not allowed.`;
    }
 
    return '';
};
 

  const validatePassword = (password) => {
    if (!password) {
      return 'Password is required.';
    }
    if (password.length < 4) {
      return 'Password must be at least 4 characters long.';
    }
    return '';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const userNameValidation = validateUserName(userName);
    const emailValidation = validateEmail(useremail);
    const passwordValidation = validatePassword(userpassword);

    if (userNameValidation || emailValidation || passwordValidation) {
      setMessage(userNameValidation || emailValidation || passwordValidation);
      return;
    }

    try {
      const response = await axios.post('http://localhost:8070/api/user/register', {
        userName: userName,
        email: useremail,
        password: userpassword,
        role: userrole,
      });

      if (response.data.success) {
        setMessage('Registration successful');
        navigate('/login'); // Redirect to the login page
      } else {
        
        setMessage(`Registration failed: ${response.data.errorMessage || 'An error occurred during registration.'}`);
      }
    } catch (error) {
      if (error.response && error.response.data) {
        console.log(error.response.data);
        
        if (error.response.data.errorMessage && error.response.data.errorMessage.includes('Already Exists')) {
           if (error.response.data.errorMessage.includes('Email')) {
            setMessage('This email address is already registered.');
          } else {
            setMessage(`Registration failed: ${error.response.data.errorMessage || error.response.data.errorMessage || 'An unexpected error occurred.'}`);
          }
        } 
      } else if (error.request) {
        setMessage('No response received from the server.');
      }
    }
  };

  return (
    <div className="authPage">
      <form onSubmit={handleSubmit} className="authForm">
        <h2>Register</h2>
        {message && <p className="errorMessage">{message}</p>}
        <div className="formGroup">
          <label>User Name:</label>
          <input
            type="text"
            value={userName}
            onChange={(e) => setUserName(e.target.value)}
            required
          />
        </div>
        <div className="formGroup">
          <label>Email:</label>
          <input
            type="email"
            value={useremail}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="formGroup">
          <label>Password:</label>
          <input
            type="password"
            value={userpassword}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div className="formGroup">
          <label>Role:</label>
          <div className="roleChoice">
            <input
              type="radio"
              id="student"
              value="STUDENT"
              checked={userrole === 'STUDENT'}
              onChange={(e) => setRole(e.target.value)}
            />
            <label htmlFor="student">Student</label>
          </div>
          <div className="roleChoice">
            <input
              type="radio"
              id="instructor"
              value="INSTRUCTOR"
              checked={userrole === 'INSTRUCTOR'}
              onChange={(e) => setRole(e.target.value)}
            />
            <label htmlFor="instructor">Instructor</label>
          </div>
        </div>
        <button type="submit" className="registerButton">Register</button>
      </form>
    </div>
  );
};

export default Register;