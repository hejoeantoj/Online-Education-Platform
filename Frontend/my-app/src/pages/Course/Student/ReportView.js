import React from 'react';
 import { Bar, Pie } from 'react-chartjs-2';

 const ReportView = ({ reportLoading, reportError, studentReportData, chartOptions }) => {
     if (reportLoading) {
         return <p>Loading report...</p>;
     }

     if (reportError) {
         return <div className="alert alert-danger">{reportError}</div>;
     }

     if (!studentReportData) {
         return <p>Report data is not yet available for this course.</p>;
     }

     const assignmentChartData = {
         labels: studentReportData.assignmentReports?.map((a, index) => a.title || `Assignment ${a.assignmentNumber || index + 1}`) || [],
         datasets: [{ label: 'Assignment Marks', data: studentReportData.assignmentReports?.map(a => a.marks ?? 0) || [], backgroundColor: 'rgba(75, 192, 192, 0.6)', borderColor: 'rgba(75, 192, 192, 1)', borderWidth: 1 }]
     };

     const quizChartData = {
         labels: studentReportData.quizReports?.map((q, index) => q.title || `Quiz ${q.quizNumber || index + 1}`) || [],
         datasets: [{ label: 'Quiz Marks', data: studentReportData.quizReports?.map(q => q.marks ?? 0) || [], backgroundColor: 'rgba(153, 102, 255, 0.6)', borderColor: 'rgba(153, 102, 255, 1)', borderWidth: 1 }]
     };

     const totalAssignments = studentReportData.assignmentReports?.length || 0;
     const totalQuizzes = studentReportData.quizReports?.length || 0;
     const totalTasks = totalAssignments + totalQuizzes;
     const completedAssignments = studentReportData.assignmentReports?.filter(a => a.marks !== null && a.marks !== undefined).length || 0;
     const completedQuizzes = studentReportData.quizReports?.filter(q => q.marks !== null && q.marks !== undefined).length || 0;
     const completedTasks = completedAssignments + completedQuizzes;
     const notCompletedTasks = totalTasks > 0 ? Math.max(0, totalTasks - completedTasks) : 0;

     const completionChartData = totalTasks > 0
         ? { labels: ['Completed/Graded', 'Pending/Not Graded'], datasets: [{ data: [completedTasks, notCompletedTasks], backgroundColor: ['rgba(54, 162, 235, 0.6)', 'rgba(255, 159, 64, 0.6)'], borderColor: ['rgba(54, 162, 235, 1)', 'rgba(255, 159, 64, 1)'], borderWidth: 1 }] }
         : { labels: ['No Tasks Yet'], datasets: [{ data: [1], backgroundColor: ['rgba(201, 203, 207, 0.6)'] }] };

     return (
         <div className="styles.reportView"> {/* You might need to import styles here if you want specific report styles */}
             <h3>Student Progress Report</h3>
             <div className="mb-4">
                 <h4>Assignments</h4>
                 {(studentReportData.assignmentReports?.length || 0) > 0 ? (
                     <>
                         <ul>{studentReportData.assignmentReports.map((a, i) => <li key={i}>{a.title || `Assignment ${a.assignmentNumber || i + 1}`}: {a.marks ?? 'Not graded'}</li>)}</ul>
                         {assignmentChartData?.labels?.length > 0 && <div style={{ height: '300px', maxWidth: '600px' }}><Bar data={assignmentChartData} options={chartOptions} /></div>}
                     </>
                 ) : <p>No assignment data available.</p>}
             </div>
             <div className="mb-4">
                 <h4>Quizzes</h4>
                 {(studentReportData.quizReports?.length || 0) > 0 ? (
                     <>
                         <ul>{studentReportData.quizReports.map((q, i) => <li key={i}>{q.title || `Quiz ${q.quizNumber || i + 1}`}: {q.marks ?? 'Not graded'}</li>)}</ul>
                         {quizChartData?.labels?.length > 0 && <div style={{ height: '300px', maxWidth: '600px' }}><Bar data={quizChartData} options={chartOptions} /></div>}
                     </>
                 ) : <p>No quiz data available.</p>}
             </div>
             <div className="mb-4">
                 <h4>Overall Completion</h4>
                 <p>{studentReportData.completedStatus || 'Status unavailable.'}</p>
                 {completionChartData?.labels?.length > 0 && <div style={{ height: '300px', maxWidth: '400px' }}><Pie data={completionChartData} options={{ ...chartOptions, maintainAspectRatio: true }} /></div>}
             </div>
         </div>
     );
 };

 export default ReportView;