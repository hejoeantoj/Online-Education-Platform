import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from 'react-router-dom';
import "./InstructorReport.css"; 

const InstructorReport = () => {
  const { courseId } = useParams();
  const [report, setReport] = useState([]);
  const [usernames, setUsernames] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [enrollmentStats, setEnrollmentStats] = useState({ enrolled: 0, completed: 0 });
  const [completionFilter, setCompletionFilter] = useState("all");//[All,Completed,Not Completed]
  const [filteredReport, setFilteredReport] = useState([]);
  const [enrolledStudents, setEnrolledStudents] = useState([]);

  const email = localStorage.getItem("email");
  const instructorId = localStorage.getItem(`${email}_uuid`);
  const token = localStorage.getItem(`${email}_token`);

  const fetchReport = async () => {
    setLoading(true);
    setError("");
    try {
      const reportResponse = await axios.get("http://localhost:8086/api/report/instructor", {
        params: { instructorId, courseId },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (reportResponse.data.success) {
        setReport(reportResponse.data.data);
        const studentIds = reportResponse.data.data.map(student => student.studentId);
        fetchUsernames(studentIds);
        fetchEnrolledStudents();
      } else {
        setError(reportResponse.data.message || "Failed to fetch report.");
      }
    } catch (err) {
      console.error(err);
      setError("Server Error. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  const fetchUsernames = async (studentIds) => {
    try {
      const response = await axios.post("http://localhost:8070/api/user/fetchUsernamesByIds", studentIds, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.data.success) {
        setUsernames(response.data.data);
      } else {
        setError(response.data.message || "Failed to fetch usernames.");
      }
    } catch (err) {
      console.error(err);
      setError("Server Error. Please try again later.");
    }
  };

  const fetchEnrolledStudents = async () => {
    try {
      const response = await axios.get(`http://localhost:8082/api/enrollment/course?courseId=${courseId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.data.success) {
        setEnrolledStudents(response.data.data);
      } else {
        console.error("Failed to fetch enrolled students:", response.data.message);
      }
    } catch (err) {
      console.error("Error fetching enrolled students:", err);
    }
  };

  useEffect(() => {
    fetchReport();
  }, [courseId, instructorId, token]);

  useEffect(() => {
    if (enrolledStudents.length > 0 && report.length > 0) {
      const completedCount = report.filter(student => student.completedStatus === "Completed").length;
      setEnrollmentStats({ enrolled: enrolledStudents.length, completed: completedCount });
    } else if (enrolledStudents.length > 0) {
      setEnrollmentStats({ enrolled: enrolledStudents.length, completed: 0 });
    }
  }, [enrolledStudents, report]);

  useEffect(() => {
    let filtered = report;
    if (completionFilter === "completed") {
      filtered = report.filter(student => student.completedStatus === "Completed");
    } else if (completionFilter === "not-completed") {
      filtered = report.filter(student => student.completedStatus === "Not Completed");
    }
    setFilteredReport(filtered);
  }, [report, completionFilter]);


  
  const downloadCSV = () => {

    const csvRows = [
      ["Student Name", "Assignment Number", "Assignment Marks", "Quiz Number", "Quiz Marks", "Completion Status"]
    ];

    filteredReport.forEach(student => {
      const assignments = student.assignmentReports.reduce((acc, curr) => {
        acc[curr.assignmentNumber] = curr.marks !== null ? curr.marks : 'Not graded yet';
        return acc;
      }, {});

      const quizzes = student.quizReports.reduce((acc, curr) => {
        acc[curr.quizNumber] = curr.marks !== null ? curr.marks : 'Not graded yet';
        return acc;
      }, {});

      const maxEntries = Math.max(student.assignmentReports.length, student.quizReports.length, 1);

      for (let i = 1; i <= maxEntries; i++) {
        csvRows.push([
          i === 1 ? (usernames[student.studentId] || student.studentId) : '',
          assignments[i] !== undefined ? i : '-',
          assignments[i] || '-',
          quizzes[i] !== undefined ? i : '-',
          quizzes[i] || '-',
          i === 1 ? student.completedStatus : ''
        ]);
      }
    });

    const csvContent = "data:text/csv;charset=utf-8," + csvRows.map(e => e.join(",")).join("\n");
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", "instructor_report.csv");
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };





  return (
    <div className="container">
      <h2 className="title">Instructor Course Report</h2>

      <div className="stats-container">
        <p><strong>Total Enrolled Students:</strong> {enrollmentStats.enrolled}</p>
        <p><strong>Completed Students:</strong> {enrollmentStats.completed}</p>
      </div>

      <div className="filter-container">
        <label htmlFor="completionFilter">Filter by Completion:</label>
        <select
          id="completionFilter"
          value={completionFilter}
          onChange={(e) => setCompletionFilter(e.target.value)}
          className="select-filter"
        >
          <option value="all">All Students</option>
          <option value="completed">Completed Students</option>
          <option value="not-completed">Not Completed Students</option>
        </select>
      </div>

      {error && <p className="error">{error}</p>}

      {filteredReport.length > 0 && (
        <>
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>Student Name</th>
                  <th>Assignment Number</th>
                  <th>Marks</th>
                  <th>Quiz Number</th>
                  <th>Marks</th>
                  <th>Completion Status</th>
                </tr>
              </thead>
              <tbody>
                {filteredReport.map((student) => {
                  const assignments = student.assignmentReports.reduce((acc, curr) => {
                    acc[curr.assignmentNumber] = curr.marks !== null ? curr.marks : 'Not graded yet';
                    return acc;
                  }, {});

                  const quizzes = student.quizReports.reduce((acc, curr) => {
                    acc[curr.quizNumber] = curr.marks !== null ? curr.marks : 'Not graded yet';
                    return acc;
                  }, {});

                  const maxEntries = Math.max(student.assignmentReports.length, student.quizReports.length, 1);

                  return Array.from({ length: maxEntries }).map((_, i) => {
                    const entryNumber = i + 1;
                    return (
                      <tr key={`student-${student.studentId}-${entryNumber}`}>
                        <td>{entryNumber === 1 ? (usernames[student.studentId] || student.studentId) : ''}</td>
                        <td>{assignments[entryNumber] !== undefined ? entryNumber : '-'}</td>
                        <td>{assignments[entryNumber] || '-'}</td>
                        <td>{quizzes[entryNumber] !== undefined ? entryNumber : '-'}</td>
                        <td>{quizzes[entryNumber] || '-'}</td>
                        <td>{entryNumber === 1 ? student.completedStatus : ''}</td>
                      </tr>
                    );
                  });
                })}
              </tbody>
            </table>
          </div>
          <button onClick={downloadCSV} className="button">
            Download Report
          </button>
        </>
      )}

      {loading && <p>Loading report...</p>}
      {filteredReport.length === 0 && !loading && !error && <p>No report data available for this course.</p>}
    </div>
  );
};

export default InstructorReport;