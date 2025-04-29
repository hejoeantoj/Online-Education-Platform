import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const CreateCourse = () => {
    const [courseTitle, setCourseTitle] = useState('');
    const [description, setDescription] = useState('');
    const [category, setCategory] = useState('');
    const [duration, setDuration] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleCreateCourse = async (e) => {
        e.preventDefault();
        const email = localStorage.getItem('email');
        const token = localStorage.getItem(`${email}_token`);
        const uuid = localStorage.getItem(`${email}_uuid`);
        console.log(token);
        console.log(uuid);

        try {
            const response = await axios.post('http://localhost:8082/api/course/create', {
                courseTitle: courseTitle,
                description: description,
                instructorId: uuid,
                category: category,
                duration: duration,
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            if (response.data.success) {
                setMessage('Course created successfully');
                navigate(`/instructor/course/${response.data.data.courseId}`); // Redirect to the course details page
            } else {
                setMessage(`Course creation failed: ${response.data.errorMessage}`);
            }
        } catch (error) {
            setMessage(`Course creation failed: ${error.response?.data?.message || error.message}`);
        }
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.heading}>Create Course</h2>
            <form onSubmit={handleCreateCourse} style={styles.form}>
                <div style={styles.formGroup}>
                    <label style={styles.label}>Course Title:</label>
                    <input
                        type="text"
                        value={courseTitle}
                        onChange={(e) => setCourseTitle(e.target.value)}
                        required
                        style={styles.input}
                    />
                </div>
                <div style={styles.formGroup}>
                    <label style={styles.label}>Description:</label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                        style={styles.input}
                    />
                </div>
                <div style={styles.formGroup}>
                    <label style={styles.label}>Category:</label>
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        required
                        style={styles.select}
                    >
                        <option value="">Select Category</option>
                        <option value="TECHNOLOGY">Technology</option>
                        <option value="BUSINESS">Business</option>
                        <option value="ARTS">Arts</option>
                        <option value="SCIENCE">Science</option>
                        <option value="HEALTH">Health</option>
                        <option value="LANGUAGE">Language</option>
                        <option value="LAW">Law</option>
                        <option value="FINANCEANDACCOUNTING">Finance And Accounting</option>
                        <option value="PHOTOGRAPHY">Photography</option>
                        <option value="MUSIC">Music</option>
                        <option value="MATH">Maths</option>
                        
                    </select>
                </div>
                <div style={styles.formGroup}>
                    <label style={styles.label}>Duration (Weeks):</label>
                    <input
                        type="number"
                        value={duration}
                        onChange={(e) => setDuration(e.target.value)}
                        required
                        style={styles.input}
                    />
                </div>
                <button type="submit" style={styles.button}>Create Course</button>
            </form>
            {message && <p style={styles.message}>{message}</p>}
        </div>
    );
};

const styles = {
    container: {
        maxWidth: '600px',
        margin: '30px auto',
        padding: '20px',
        border: '1px solid #ccc',
        borderRadius: '8px',
        backgroundColor: '#f9f9f9',
    },
    heading: {
        textAlign: 'center',
        color: '#333',
        marginBottom: '20px',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
    },
    formGroup: {
        marginBottom: '15px',
    },
    label: {
        display: 'block',
        marginBottom: '5px',
        fontWeight: 'bold',
        color: '#555',
    },
    input: {
        width: '100%',
        padding: '10px',
        border: '1px solid #ddd',
        borderRadius: '4px',
        boxSizing: 'border-box',
        fontSize: '1rem',
    },
    select: {
        width: '100%',
        padding: '10px',
        border: '1px solid #ddd',
        borderRadius: '4px',
        boxSizing: 'border-box',
        fontSize: '1rem',
        backgroundColor: 'white',
    },
    button: {
        backgroundColor: '#007bff',
        color: 'white',
        padding: '10px 15px',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
        fontSize: '1rem',
        transition: 'background-color 0.3s ease',
    },
    buttonHover: {
        backgroundColor: '#0056b3',
    },
    message: {
        marginTop: '20px',
        textAlign: 'center',
        fontWeight: 'bold',
        color: 'green',
    },
};

export default CreateCourse;
