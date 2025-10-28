import { useAuth } from "../hooks/useAuth";

export const Navbar = () => {
  const { user, logout } = useAuth();

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-danger">
      <div className="container-fluid">
        <a className="navbar-brand" href="/">
          <img src="/logo-porto.png" height="30" alt="P.PORTO" />
        </a>

        <div className="collapse navbar-collapse">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <a className="nav-link" href="/cursos">Cursos</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/departamentos">Departamentos</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/servicos">Serviços</a>
            </li>
          </ul>

          <div className="d-flex align-items-center">
            <div className="text-white me-3">{user?.name}</div>
            <button className="btn btn-outline-light" onClick={logout}>
              Sair
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};