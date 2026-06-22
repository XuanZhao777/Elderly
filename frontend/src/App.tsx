import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import AppLayout from './components/Layout';
import Home from './pages/Home';
import ElderlyDetail from './pages/ElderlyDetail';
import CheckIn from './pages/CheckIn';
import Profile from './pages/Profile';
import './App.css';

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <AppLayout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/elderly/:id" element={<ElderlyDetail />} />
          <Route path="/checkin" element={<CheckIn />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AppLayout>
    </BrowserRouter>
  );
};

export default App;
