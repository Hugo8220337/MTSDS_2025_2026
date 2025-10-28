import { NavLink } from 'react-router-dom';

export const Sidebar = () => {
  return (
    <div className="bg-white shadow-sm" style={{ width: '250px', minHeight: '100%' }}>
      <div className="p-3">
        <div className="list-group list-group-flush">
          <NavLink to="/area-pessoal" className="list-group-item list-group-item-action">
            <i className="bi bi-person me-2"></i>
            Área Pessoal
          </NavLink>
          <NavLink to="/atividade-letiva" className="list-group-item list-group-item-action">
            <i className="bi bi-book me-2"></i>
            Atividade Letiva
          </NavLink>
          <NavLink to="/area-financeira" className="list-group-item list-group-item-action">
            <i className="bi bi-wallet me-2"></i>
            Área Financeira
          </NavLink>
          <NavLink to="/certidoes" className="list-group-item list-group-item-action">
            <i className="bi bi-file-text me-2"></i>
            Certidões/Diplomas
          </NavLink>
          <NavLink to="/inscricoes" className="list-group-item list-group-item-action">
            <i className="bi bi-pencil-square me-2"></i>
            Inscrições
          </NavLink>
        </div>
      </div>
    </div>
  );
};