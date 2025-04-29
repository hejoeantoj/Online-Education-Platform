import React from 'react';
import { Link } from 'react-router-dom';

const Footer = ({ links }) => {
  const currentYear = new Date().getFullYear();

  const footerStyle = {
    backgroundColor: '#f8f9fa', // Light gray background
    padding: '15px 20px',
    position: 'fixed',
    bottom: 0,
    left: 0,
    width: '100%',
    borderTop: '1px solid #ddd',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    fontSize: '14px',
    color: '#555',
    zIndex: 1000, // Ensure it stays on top of content
  };

  const leftStyle = {
    display: 'flex',
    alignItems: 'center',
  };

  const navStyle = {
    display: 'flex',
    gap: '15px',
  };

  const linkStyle = {
    textDecoration: 'none',
    color: '#007bff', // Primary blue color
  };

  return (
    <footer style={footerStyle}>
      <div style={{height:'20px'}}>
        <p style={{marginLeft:'500px'}}>&copy; {currentYear} OEP. All rights reserved.</p>
      </div>
      {links && links.length > 0 && (
        <nav style={navStyle}>
          {links.map((link, index) => (
            <Link key={index} to={link.to} style={linkStyle}>
              {link.label}
            </Link>
          ))}
        </nav>
      )}
    </footer>
  );
};

export default Footer;