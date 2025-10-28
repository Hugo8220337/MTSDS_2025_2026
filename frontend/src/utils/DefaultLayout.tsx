import { Outlet } from 'react-router-dom';
import { Navbar } from './Navbar';
import { Sidebar } from './Sidebar';

export const DefaultLayout = () => {
  return (
    <div className="min-vh-100 d-flex flex-column">
      <Navbar />
      <div className="d-flex flex-grow-1">
        <Sidebar />
        <main className="flex-grow-1 bg-light p-4">
          <Outlet />
        </main>
      </div>
    </div>
  );
};