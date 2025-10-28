import { useState } from 'react';

interface Schedule {
  id: number;
  time: string;
  course: string;
  room: string;
}

export const DashboardPage = () => {
  const [todaySchedules] = useState<Schedule[]>([
    {
      id: 1,
      time: '18:00 - 20:00',
      course: 'MEI_MTSDS - MEI - NC',
      room: '1º Ano'
    },
    {
      id: 2,
      time: '20:00 - 22:00',
      course: 'MEI_SO - MEI - NC',
      room: '1º Ano'
    }
  ]);

  const [tomorrowSchedules] = useState<Schedule[]>([
    {
      id: 3,
      time: '18:00 - 20:00',
      course: 'MEI_CDN - MEI - MEI1T1',
      room: '1º Ano'
    },
    {
      id: 4,
      time: '20:00 - 22:00',
      course: 'MEI_MTSDS - MEI - MEI1T1',
      room: '1º Ano'
    }
  ]);

  return (
    <div className="container-fluid">
      <div className="row g-4">
        {/* Today's Schedule */}
        <div className="col-12 col-lg-6">
          <div className="card shadow-sm h-100">
            <div className="card-header bg-danger text-white">
              <div className="d-flex justify-content-between align-items-center">
                <h5 className="mb-0">HOJE</h5>
                <span className="fs-4">28</span>
              </div>
              <small>2º SEMESTRE</small>
            </div>
            <div className="card-body">
              {todaySchedules.map((schedule) => (
                <div key={schedule.id} className="mb-3 p-3 border-bottom">
                  <div className="d-flex justify-content-between">
                    <strong>{schedule.time}</strong>
                    <span>{schedule.room}</span>
                  </div>
                  <div>{schedule.course}</div>
                </div>
              ))}
            </div>
            <div className="card-footer bg-light">
              <small className="text-muted">1/2</small>
            </div>
          </div>
        </div>

        {/* Tomorrow's Schedule */}
        <div className="col-12 col-lg-6">
          <div className="card shadow-sm h-100">
            <div className="card-header bg-danger text-white">
              <div className="d-flex justify-content-between align-items-center">
                <h5 className="mb-0">AMANHÃ</h5>
                <span className="fs-4">29</span>
              </div>
              <small>1º SEMESTRE</small>
            </div>
            <div className="card-body">
              {tomorrowSchedules.map((schedule) => (
                <div key={schedule.id} className="mb-3 p-3 border-bottom">
                  <div className="d-flex justify-content-between">
                    <strong>{schedule.time}</strong>
                    <span>{schedule.room}</span>
                  </div>
                  <div>{schedule.course}</div>
                </div>
              ))}
            </div>
            <div className="card-footer bg-light">
              <small className="text-muted">2/2</small>
            </div>
          </div>
        </div>

        {/* Quick Access Icons */}
        <div className="col-12">
          <div className="d-flex gap-4 justify-content-center mt-4">
            <div className="text-center">
              <div className="bg-secondary p-3 rounded-circle mb-2">
                <i className="bi bi-mortarboard text-white fs-4"></i>
              </div>
              <small>Pedido de Diploma</small>
            </div>
            <div className="text-center">
              <div className="bg-secondary p-3 rounded-circle mb-2">
                <i className="bi bi-bicycle text-white fs-4"></i>
              </div>
              <small>Atribuição de BIPP</small>
            </div>
            <div className="text-center">
              <div className="bg-secondary p-3 rounded-circle mb-2">
                <i className="bi bi-file-text text-white fs-4"></i>
              </div>
              <small>Pedido de Certidão</small>
            </div>
            <div className="text-center">
              <div className="bg-secondary p-3 rounded-circle mb-2">
                <i className="bi bi-file-earmark text-white fs-4"></i>
              </div>
              <small>Requerimentos</small>
            </div>
            <div className="text-center">
              <div className="bg-secondary p-3 rounded-circle mb-2">
                <i className="bi bi-card-list text-white fs-4"></i>
              </div>
              <small>Notas</small>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};