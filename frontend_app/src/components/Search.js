// src/components/SearchInput.js
import React, { useState } from 'react';
import './Header.css'; // Assuming Header.css contains relevant styles

function SearchInput({ onSearch }) {
  const [searchText, setSearchText] = useState('');

  const handleInputChange = (event) => {
    const newSearchText = event.target.value;
    setSearchText(newSearchText);
    onSearch(newSearchText); // Call onSearch on every input change
  };

  const handleSearchSubmit = (event) => {
    event.preventDefault();
    // You might still want to handle the submit event for other reasons,
    // like clearing the input or specific actions on pressing Enter.
    // However, the search is already happening in handleInputChange.
  };

  return (
    <form className="search-form" onSubmit={handleSearchSubmit}>
      <input
        type="text"
        className="search-input"
        placeholder="Search..."
        value={searchText}
        onChange={handleInputChange}
      />
      <button type="submit" className="search-button">Search</button>
    </form>
  );
}

export default SearchInput;