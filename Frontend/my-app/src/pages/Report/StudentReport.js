import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { Bar, Pie } from 'react-chartjs-2';
import 'chart.js/auto';
import './StudentReport.css';

const StudentReport = () => {
  const { courseId } = useParams();
  const [report, setReport] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true); 

  const email = localStorage.getItem("email");
  const user = localStorage.getItem(`${email}_uuid`);
  const token = localStorage.getItem(`${email}_token`);

  const fetchStudentReport = async () => {
    setLoading(true); 
    try {
      const response = await axios.get(
        `http://localhost:8086/api/report/student?studentId=${user}&courseId=${courseId}`,
        {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }
      );
      setReport(response.data.data);
    } catch (err) {
      console.error('Error fetching student report:', err);
      setError('No Assignments and Quizzes For this Course. Please wait until the instructor adds them.');
      setReport(null); 
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStudentReport();
  }, [courseId, user, token]);

  if (loading) {
    return <div className="loading-message">Loading...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (!report) {
    return <div>No report data available.</div>; // Handle case where report is still null after loading
  }

  const assignmentMarks = report.assignmentReports.map(a => a.marks !== null ? a.marks : 0);
  const quizMarks = report.quizReports.map(q => q.marks !== null ? q.marks : 0);

  const totalTasks = report.assignmentReports.length + report.quizReports.length;
  const completedTasks = report.assignmentReports.filter(a => a.marks !== null).length + report.quizReports.filter(q => q.marks !== null).length;

  const assignmentData = {
    labels: report.assignmentReports.map(a => `Assignment ${a.assignmentNumber}`),
    datasets: [
      {
        label: 'Assignment Marks',
        data: assignmentMarks,
        backgroundColor: 'rgba(75, 192, 192, 0.6)',
      }
    ]
  };

  const quizData = {
    labels: report.quizReports.map(q => `Quiz ${q.quizNumber}`),
    datasets: [
      {
        label: 'Quiz Marks',
        data: quizMarks,
        backgroundColor: 'rgba(153, 102, 255, 0.6)',
      }
    ]
  };

  const completionData = {
    labels: ['Completed', 'Not Completed'],
    datasets: [
      {
        data: [completedTasks, totalTasks - completedTasks],
        backgroundColor: ['rgba(54, 162, 235, 0.6)', 'rgba(255, 99, 132, 0.6)'],
      }
    ]
  };

  const chartOptions = {
    maintainAspectRatio: false,
    responsive: true,
  };

  return (
    <div className="student-report-container">
      <h2>Student Course Report</h2>

      <div className="report-section">
        <h3>Assignments</h3>
        <ul>
          {report.assignmentReports.map((assignment, index) => (
            <li key={index}>
              Assignment {assignment.assignmentNumber}:{' '}
              {assignment.marks !== null ? assignment.marks : 'Not graded yet'}
            </li>
          ))}
        </ul>
        {report.assignmentReports.length > 0 && (
          <div className="chart-container" style={{ height: '300px' }}>
            <Bar data={assignmentData} options={chartOptions} />
          </div>
        )}
        {report.assignmentReports.length === 0 && <p>No assignments available.</p>}
      </div>

      <div className="report-section">
        <h3>Quizzes</h3>
        <ul>
          {report.quizReports.map((quiz, index) => (
            <li key={index}>
              Quiz {quiz.quizNumber}:{' '}
              {quiz.marks !== null ? quiz.marks : 'Not attended yet'}
            </li>
          ))}
        </ul>
        {report.quizReports.length > 0 && (
          <div className="chart-container" style={{ height: '300px' }}>
            <Bar data={quizData} options={chartOptions} />
          </div>
        )}
        {report.quizReports.length === 0 && <p>No quizzes available.</p>}
      </div>

      <div className="report-section completion-status">
        <h3>Completion Status</h3>
        <p>{report.completedStatus}</p>
        <div className="chart-container" style={{ height: '300px' }}>
          <Pie data={completionData} options={{ ...chartOptions, plugins: { legend: { display: true } } }} />
        </div>
      </div>
    </div>
  );
};

export default StudentReport;
