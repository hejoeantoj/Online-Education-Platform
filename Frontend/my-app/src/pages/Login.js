import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Auth.css'; // Import the CSS file for styling

const Login = () => {

  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('INSTRUCTOR'); // Default role is INSTRUCTOR
  
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');
   

    if (!email || !password) {
      setMessage('Please fill in all fields.');
      return;
    }

    try {
      const response = await axios.post('http://localhost:8070/api/user/login', {
        email,
        password,
        role,
      });

      if (response.data.success) {
        setMessage('Login successful! Redirecting...');
        const { token, uuid } = response.data.data;

        // Store details in localStorage
        localStorage.setItem(`${email}_token`, token);
        localStorage.setItem(`${email}_uuid`, uuid);
        localStorage.setItem('email', email);
        localStorage.setItem('role', role); // Store role to identify later

        // Redirect based on role
        if (role === 'INSTRUCTOR') {
          navigate('/instructor'); // Redirect to Instructor Dashboard
        } else if (role === 'STUDENT') {
          navigate('/student'); // Redirect to Student Dashboard
        }
      } else {
        console.log(response);
        alert(`Login failed: ${response.data.message}`);
      }
    } catch (error) {
      setMessage('Login failed. Please check your credentials.');
      //console.error('Login error:', error);
      if (error.response && error.response.status === 401) {
        setMessage('Invalid credentials. Please try again.');
      } else if (error.response && error.response.status === 400) {
        setMessage('Check your email and password.');
      }
    }
  };
  
  useEffect(() => {
    // Store details in localStorage
    localStorage.removeItem( `${email}_token`);
    localStorage.removeItem(`${email}_uuid`);
    localStorage.removeItem("email");
    localStorage.removeItem( "role");
  }, []);

  
  return (
    <div className="authPage">
      <form onSubmit={handleSubmit} className="authForm">
        <h2>Login</h2>
        <div className="formGroup">
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="formGroup">
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            
          />
          
        </div>
        <div className="roleChoice">
          <input
            type="radio"
            id="student"
            value="STUDENT"
            checked={role === 'STUDENT'}
            onChange={(e) => setRole(e.target.value)}
          />
          <label htmlFor="student">Student</label>
        </div>
        <div className="roleChoice">
          <input
            type="radio"
            id="instructor"
            value="INSTRUCTOR"
            checked={role === 'INSTRUCTOR'}
            onChange={(e) => setRole(e.target.value)}
          />
          <label htmlFor="instructor">Instructor</label>
        </div>

        <button type="submit" className="loginButton">Login</button>
        <p className="registerLink">
          New User? <a href="/register">Register here</a>
        </p>
        {message && <p className="errorMessage">{message}</p>}
      </form>
    </div>
  );
};

export default Login;