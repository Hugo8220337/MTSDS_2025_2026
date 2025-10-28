import { useState } from 'react';

export const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // Add your login logic here
        console.log('Login attempted:', username);
    };

    return (
        <div className="container-fluid min-vh-100 d-flex align-items-center justify-content-center bg-light">
            <div className="card shadow-lg" style={{ maxWidth: '400px', width: '100%' }}>
                <div className="card-body p-5">
                    <div className="text-center mb-4">
                        {/* TODO Replace with logo */}
                        <h2 className="text-danger mb-4">SUMOD</h2>
                    </div>
                    
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <input
                                type="text"
                                className="form-control form-control-lg"
                                placeholder="Username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>
                        
                        <div className="mb-4">
                            <input
                                type="password"
                                className="form-control form-control-lg"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>

                        <div className="d-grid gap-2">
                            <button 
                                type="submit" 
                                className="btn btn-danger btn-lg"
                            >
                                Entrar
                            </button>
                        </div>

                        <div className="text-center mt-3">
                            <a href="#" className="text-muted text-decoration-none">
                                Recuperar Palavra-passe
                            </a>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    );
};