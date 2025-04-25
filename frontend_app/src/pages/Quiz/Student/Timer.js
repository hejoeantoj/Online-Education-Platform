// Timer.js
import React, { useEffect, useState, useRef } from 'react';

const Timer = ({ duration, onTimeUp, isRunning }) => {
    const [timeLeft, setTimeLeft] = useState(duration);
    const timerIdRef = useRef(null);

    useEffect(() => {
        if (!isRunning) {
            clearInterval(timerIdRef.current);
            return;
        }

        if (timeLeft <= 0) {
            onTimeUp();
            return;
        }

        timerIdRef.current = setInterval(() => {
            setTimeLeft(prevTime => {
                return prevTime - 1;
            });
        }, 1000);

        return () => {
            clearInterval(timerIdRef.current);
        };
    }, [timeLeft, onTimeUp, isRunning]);

    const formatTime = (seconds) => {
        const minutes = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
    };

    return (
        <div className="timer">
            Time Left: {formatTime(timeLeft)}
        </div>
    );
};

export default Timer;