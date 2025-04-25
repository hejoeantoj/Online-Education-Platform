import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ element, requiredRole }) => {
  const email = localStorage.getItem('email');
  const token = localStorage.getItem(`${email}_token`);
  const role = localStorage.getItem('role');

  // If not logged in or token is invalid
  if (!email || !token) {
    return <Navigate to="/login" />;
  }

  console.log(requiredRole);

  // If role doesn't match any of the required roles
  if (requiredRole && !requiredRole.includes(role)) {
    alert('You are not authorized to view this page.');
    return <Navigate to="/login" />;
  }

  // Render the component if the user is authorized
  return element;
};

export default ProtectedRoute;
