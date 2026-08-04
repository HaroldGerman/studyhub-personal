import React, { FormEvent, useEffect, useMemo, useState, useRef } from 'react';
import { createRoot } from 'react-dom/client';
import {
  BookOpen, CalendarDays, CheckCircle2, ChevronRight, FileText, LayoutDashboard,
  MoreHorizontal, Plus, Search, Settings, Sparkles, Star, X, ArrowLeft, Trash2,
  Edit, Save, LogOut, Bell, Check, Moon, Sun, Filter, Lock, User, Clock,
  Maximize2, Minimize2
} from 'lucide-react';
import './styles.css';

// TypeScript Type Definitions
interface UserProfile {
  name: string;
  email: string;
}

interface Course {
  id: string;
  title: string;
  code: string;
  description: string;
  professor: string;
  university: string;
  platform: string;
  startDate: string;
  endDate: string;
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED' | 'PAUSED';
  color: string;
  icon: string;
  lessons: number;
  completed: number;
  progress: number;
}

interface Lesson {
  id: string;
  title: string;
  completed: boolean;
}

interface Note {
  id: string;
  title: string;
  body: string;
  scratchpad?: string;
  lastModified: string;
  courseId?: string;
}

interface CalendarEvent {
  id: string;
  title: string;
  description: string;
  dateTime: string; // ISO LocalDateTime
  color: string;
}

// API Fetch Helper
const API_BASE_URL = (import.meta as any).env?.VITE_API_URL || '';

const getAuthHeaders = (): Record<string, string> => {
  const token = localStorage.getItem('sh-token');
  return token ? { 'Authorization': `Bearer ${token}` } : {};
};

async function apiFetch<T>(url: string, options: RequestInit = {}): Promise<T> {
  const headers = {
    'Content-Type': 'application/json',
    ...getAuthHeaders(),
    ...(options.headers || {}),
  };

  const response = await fetch(`${API_BASE_URL}${url}`, { ...options, headers });
  
  if (response.status === 401) {
    localStorage.removeItem('sh-token');
    localStorage.removeItem('sh-user');
    window.location.reload();
    throw new Error('Sesión expirada');
  }

  if (!response.ok) {
    let errMsg = 'Error en la solicitud';
    try {
      const errData = await response.json();
      errMsg = errData.message || errMsg;
    } catch (_) {}
    throw new Error(errMsg);
  }

  // Handle 204 No Content
  if (response.status === 204) {
    return {} as T;
  }

  return response.json();
}

// Login & Register Component
function Login({ done }: { done: (token: string, email: string) => void }) {
  const [isRegister, setIsRegister] = useState(false);
  const [showVerify, setShowVerify] = useState(false);
  const [verifyEmail, setVerifyEmail] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const submit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    const f = new FormData(e.currentTarget);
    const email = String(f.get('email') || '');
    const password = String(f.get('password') || '');
    const name = String(f.get('name') || '');

    try {
      if (isRegister) {
        const res = await apiFetch<{ verificationRequired: boolean; email: string }>(`/api/auth/register`, {
          method: 'POST',
          body: JSON.stringify({ name, email, password }),
        });
        if (res.verificationRequired) {
          setVerifyEmail(res.email);
          setShowVerify(true);
          setSuccess('Cuenta registrada. Por favor ingresa el código de 6 dígitos enviado a tu correo.');
        } else {
          setSuccess('Cuenta creada con éxito.');
        }
      } else {
        const res = await apiFetch<{ accessToken: string }>(`/api/auth/login`, {
          method: 'POST',
          body: JSON.stringify({ email, password }),
        });
        done(res.accessToken, email);
      }
    } catch (err: any) {
      setError(err.message || 'Error de conexión con el servidor');
    } finally {
      setLoading(false);
    }
  };

  const submitVerify = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    const f = new FormData(e.currentTarget);
    const code = String(f.get('code') || '');

    try {
      const res = await apiFetch<{ accessToken: string }>(`/api/auth/verify`, {
        method: 'POST',
        body: JSON.stringify({ email: verifyEmail, code }),
      });
      setSuccess('Cuenta verificada con éxito. Iniciando sesión...');
      setTimeout(() => {
        done(res.accessToken, verifyEmail);
      }, 1000);
    } catch (err: any) {
      setError(err.message || 'Código inválido o expirado');
    } finally {
      setLoading(false);
    }
  };

  if (showVerify) {
    return (
      <div className="login-page">
        <div className="login-card glass">
          <div className="brand">
            <span className="brandmark">S</span>studyhub
          </div>
          <h1>Verifica tu cuenta</h1>
          <p className="muted">
            Hemos enviado un código de 6 dígitos a <b>{verifyEmail}</b>. Ingrésalo a continuación para activar tu espacio.
          </p>

          <form onSubmit={submitVerify}>
            <label>
              Código de Verificación
              <div className="input-with-icon">
                <Lock size={16} />
                <input required maxLength={6} minLength={6} name="code" placeholder="123456" style={{ letterSpacing: '4px', textAlign: 'center', fontSize: '18px', fontWeight: 'bold' }} />
              </div>
            </label>

            {error && <div className="error-alert">{error}</div>}
            {success && <div className="success-alert">{success}</div>}

            <button className="primary submit-btn" type="submit" disabled={loading}>
              {loading ? 'Verificando...' : 'Activar Cuenta'}
            </button>
          </form>

          <div className="login-footer">
            <button className="link-btn" onClick={() => { setShowVerify(false); setError(''); setSuccess(''); }}>
              Volver al inicio de sesión
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="login-page">
      <div className="login-card glass">
        <div className="brand">
          <span className="brandmark">S</span>studyhub
        </div>
        <h1>{isRegister ? 'Crea tu acceso' : 'Estudia sin límites'}</h1>
        <p className="muted">
          {isRegister ? 'Completa los campos para crear tu espacio personal.' : 'Bienvenido de nuevo a tu espacio de estudio.'}
        </p>

        <form onSubmit={submit}>
          {isRegister && (
            <label>
              Nombre Completo
              <div className="input-with-icon">
                <User size={16} />
                <input required name="name" placeholder="Harold German" />
              </div>
            </label>
          )}

          <label>
            Correo Electrónico
            <div className="input-with-icon">
              <User size={16} />
              <input required type="email" name="email" placeholder="harold@studyhub.local" />
            </div>
          </label>

          <label>
            Contraseña
            <div className="input-with-icon">
              <Lock size={16} />
              <input required minLength={6} name="password" type="password" placeholder="••••••••" />
            </div>
          </label>

          {error && <div className="error-alert">{error}</div>}
          {success && <div className="success-alert">{success}</div>}

          <button className="primary submit-btn" type="submit" disabled={loading}>
            {loading ? 'Procesando...' : isRegister ? 'Crear Cuenta' : 'Entrar'}
          </button>
        </form>

        <div className="login-footer">
          <button className="link-btn" onClick={() => { setIsRegister(!isRegister); setError(''); setSuccess(''); }}>
            {isRegister ? '¿Ya tienes una cuenta? Inicia sesión' : '¿No tienes cuenta? Registrate gratis'}
          </button>
        </div>
      </div>
    </div>
  );
}

// Global Command Palette Search Modal
function SearchModal({
  isOpen,
  onClose,
  courses,
  notes,
  onSelectCourse,
  onSelectNote
}: {
  isOpen: boolean;
  onClose: () => void;
  courses: Course[];
  notes: Note[];
  onSelectCourse: (id: string) => void;
  onSelectNote: (id: string) => void;
}) {
  const [q, setQ] = useState('');
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (isOpen) {
      setQ('');
      setTimeout(() => inputRef.current?.focus(), 100);
    }
  }, [isOpen]);

  const filteredCourses = useMemo(() => {
    if (!q) return [];
    return courses.filter(c => c.title.toLowerCase().includes(q.toLowerCase()) || c.code.toLowerCase().includes(q.toLowerCase()));
  }, [courses, q]);

  const filteredNotes = useMemo(() => {
    if (!q) return [];
    return notes.filter(n => n.title.toLowerCase().includes(q.toLowerCase()) || n.body.toLowerCase().includes(q.toLowerCase()));
  }, [notes, q]);

  if (!isOpen) return null;

  return (
    <div className="overlay" onClick={onClose}>
      <div className="search-modal glass" onClick={e => e.stopPropagation()}>
        <div className="search-modal-header">
          <Search size={20} />
          <input
            ref={inputRef}
            value={q}
            onChange={e => setQ(e.target.value)}
            placeholder="Buscar cursos o notas..."
          />
          <button className="close-btn" onClick={onClose}><X size={18} /></button>
        </div>
        <div className="search-modal-results">
          {q && filteredCourses.length === 0 && filteredNotes.length === 0 && (
            <div className="empty-results">No se encontraron resultados para "{q}"</div>
          )}
          {!q && <div className="search-tip">Escribe algo para buscar en tu espacio de estudio...</div>}
          
          {filteredCourses.length > 0 && (
            <div className="result-section">
              <h4>Cursos</h4>
              {filteredCourses.map(c => (
                <div key={c.id} className="result-item" onClick={() => { onSelectCourse(c.id); onClose(); }}>
                  <span className="result-icon" style={{ background: c.color }}>{c.icon}</span>
                  <div>
                    <div className="result-title">{c.title}</div>
                    <div className="result-meta">{c.code} · {c.professor}</div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {filteredNotes.length > 0 && (
            <div className="result-section">
              <h4>Notas</h4>
              {filteredNotes.map(n => (
                <div key={n.id} className="result-item" onClick={() => { onSelectNote(n.id); onClose(); }}>
                  <span className="result-icon notes-icon">⌘</span>
                  <div>
                    <div className="result-title">{n.title}</div>
                    <div className="result-meta">{n.body.substring(0, 60)}...</div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

// Simple Markdown Parser function
function parseMarkdown(text: string): React.ReactNode[] {
  if (!text) return [];
  const lines = text.split('\n');
  const elements: React.ReactNode[] = [];
  let i = 0;

  // Simple inline parser helper
  const parseInline = (str: string): React.ReactNode => {
    const parts = str.split('**');
    const nodes: React.ReactNode[] = [];
    parts.forEach((part, index) => {
      if (index % 2 === 1) {
        nodes.push(<strong key={index}>{part}</strong>);
      } else {
        const codeParts = part.split('`');
        codeParts.forEach((cp, cIndex) => {
          if (cIndex % 2 === 1) {
            nodes.push(<code key={`c-${cIndex}`} className="md-inline-code">{cp}</code>);
          } else {
            nodes.push(cp);
          }
        });
      }
    });
    return <>{nodes}</>;
  };

  while (i < lines.length) {
    const line = lines[i];
    const trimmed = line.trim();

    // 1. Code Blocks
    if (trimmed.startsWith('```')) {
      const lang = trimmed.slice(3).trim();
      const codeLines = [];
      i++;
      while (i < lines.length && !lines[i].trim().startsWith('```')) {
        codeLines.push(lines[i]);
        i++;
      }
      elements.push(
        <pre key={i} className="md-codeblock">
          <code className={lang ? `language-${lang}` : ''}>{codeLines.join('\n')}</code>
        </pre>
      );
      i++; // skip closing ```
      continue;
    }

    // 2. Blockquotes
    if (trimmed.startsWith('> ')) {
      const quoteLines = [];
      while (i < lines.length && lines[i].trim().startsWith('> ')) {
        quoteLines.push(lines[i].trim().slice(2));
        i++;
      }
      elements.push(
        <blockquote key={i} className="md-quote">
          {quoteLines.map((l, lIdx) => <p key={lIdx}>{parseInline(l)}</p>)}
        </blockquote>
      );
      continue;
    }

    // 3. Tables
    if (trimmed.startsWith('|')) {
      const tableLines = [];
      while (i < lines.length && lines[i].trim().startsWith('|')) {
        tableLines.push(lines[i].trim());
        i++;
      }
      if (tableLines.length > 0) {
        const rows = tableLines.map(rowStr => {
          const parts = rowStr.split('|').map(p => p.trim());
          if (parts[0] === '') parts.shift();
          if (parts[parts.length - 1] === '') parts.pop();
          return parts;
        });

        let hasHeader = false;
        let startRowIdx = 0;
        if (rows.length > 1 && rows[1].every(cell => cell.startsWith('-') || cell.endsWith('-') || cell.includes(':'))) {
          hasHeader = true;
          startRowIdx = 2;
        }

        const headers = hasHeader ? rows[0] : null;
        const bodyRows = rows.slice(startRowIdx);

        elements.push(
          <div key={i} className="md-table-container" style={{ overflowX: 'auto', margin: '12px 0' }}>
            <table className="md-table" style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid var(--border-color)', borderRadius: '6px' }}>
              {headers && (
                <thead>
                  <tr style={{ background: 'rgba(255,255,255,0.05)', borderBottom: '2px solid var(--border-color)' }}>
                    {headers.map((h, hIdx) => (
                      <th key={hIdx} style={{ padding: '8px 12px', textAlign: 'left', fontWeight: 'bold', fontSize: '13px' }}>{parseInline(h)}</th>
                    ))}
                  </tr>
                </thead>
              )}
              <tbody>
                {bodyRows.map((r, rIdx) => (
                  <tr key={rIdx} style={{ borderBottom: '1px solid var(--border-color)' }}>
                    {r.map((c, cIdx) => (
                      <td key={cIdx} style={{ padding: '8px 12px', fontSize: '13px' }}>{parseInline(c)}</td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        );
        continue;
      }
    }

    // 4. Unordered Lists
    if (trimmed.startsWith('- ') || trimmed.startsWith('* ')) {
      const listItems = [];
      while (i < lines.length && (lines[i].trim().startsWith('- ') || lines[i].trim().startsWith('* '))) {
        listItems.push(lines[i].trim().slice(2));
        i++;
      }
      elements.push(
        <ul key={i} className="md-ul" style={{ paddingLeft: '20px', margin: '8px 0' }}>
          {listItems.map((li, liIdx) => (
            <li key={liIdx} className="md-li" style={{ marginBottom: '4px' }}>{parseInline(li)}</li>
          ))}
        </ul>
      );
      continue;
    }

    // 5. Headers
    if (trimmed.startsWith('# ')) {
      elements.push(<h1 key={i} className="md-h1">{parseInline(trimmed.slice(2))}</h1>);
      i++;
      continue;
    }
    if (trimmed.startsWith('## ')) {
      elements.push(<h2 key={i} className="md-h2">{parseInline(trimmed.slice(3))}</h2>);
      i++;
      continue;
    }
    if (trimmed.startsWith('### ')) {
      elements.push(<h3 key={i} className="md-h3">{parseInline(trimmed.slice(4))}</h3>);
      i++;
      continue;
    }

    // 6. Empty Line
    if (trimmed === '') {
      elements.push(<div key={i} className="md-empty" style={{ height: '0.8em' }} />);
      i++;
      continue;
    }

    // 7. Paragraph
    elements.push(<p key={i} className="md-p">{parseInline(line)}</p>);
    i++;
  }

  return elements;
}

// App shell Component
function App() {
  const [token, setToken] = useState(() => localStorage.getItem('sh-token') || '');
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [page, setPage] = useState('dashboard');
  const [courses, setCourses] = useState<Course[]>([]);
  const [notes, setNotes] = useState<Note[]>([]);
  const [events, setEvents] = useState<CalendarEvent[]>([]);
  const [selectedCourseId, setSelectedCourseId] = useState<string | null>(null);
  const [selectedNoteId, setSelectedNoteId] = useState<string | null>(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [searchOpen, setSearchOpen] = useState(false);
  const [dark, setDark] = useState(() => localStorage.getItem('sh-dark') === 'true');
  const [activityLog, setActivityLog] = useState<{ id: string; text: string; time: string; icon: string }[]>([]);

  // Fetch initial profile and items on login/load
  useEffect(() => {
    if (token) {
      fetchData();
    }
  }, [token]);

  // Track keyboard shortcuts
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
        e.preventDefault();
        setSearchOpen(prev => !prev);
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  const fetchData = async () => {
    try {
      const prof = await apiFetch<UserProfile>('/api/auth/me');
      setProfile(prof);
      
      const courseList = await apiFetch<Course[]>('/api/courses');
      setCourses(courseList);

      const noteList = await apiFetch<Note[]>('/api/notes');
      setNotes(noteList);

      const eventList = await apiFetch<CalendarEvent[]>('/api/events');
      setEvents(eventList);

      // Generate activity log
      const logs = [];
      if (courseList.length > 0) {
        logs.push({
          id: '1',
          text: `Visualizando curso "${courseList[0].title}"`,
          time: 'Hace unos momentos',
          icon: 'BookOpen'
        });
      }
      if (noteList.length > 0) {
        logs.push({
          id: '2',
          text: `Última edición: "${noteList[0].title}"`,
          time: 'Hoy',
          icon: 'FileText'
        });
      }
      setActivityLog(logs);

    } catch (err) {
      console.error('Error fetching data:', err);
    }
  };

  const handleLogin = (newToken: string, email: string) => {
    localStorage.setItem('sh-token', newToken);
    localStorage.setItem('sh-user', email);
    setToken(newToken);
    setPage('dashboard');
  };

  const handleLogout = () => {
    localStorage.removeItem('sh-token');
    localStorage.removeItem('sh-user');
    setToken('');
    setProfile(null);
  };

  const addCourse = async (form: FormData) => {
    try {
      const title = String(form.get('title') || 'Nuevo curso');
      const code = String(form.get('code') || 'SIN-CÓDIGO');
      const professor = String(form.get('professor') || 'Por asignar');
      const university = String(form.get('university') || 'Universidad');
      const platform = String(form.get('platform') || 'Plataforma');
      const color = String(form.get('color') || '#7257e8');
      const icon = String(form.get('icon') || '✦');
      
      const newCourse = await apiFetch<Course>('/api/courses', {
        method: 'POST',
        body: JSON.stringify({
          title, code, professor, university, platform, color, icon,
          status: 'NOT_STARTED',
          startDate: new Date().toISOString().split('T')[0],
          endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
        })
      });

      setCourses(prev => [...prev, newCourse]);
      setModalOpen(false);
      
      // Update activity log
      setActivityLog(prev => [
        { id: Date.now().toString(), text: `Agregaste el curso "${title}"`, time: 'Ahora mismo', icon: 'Plus' },
        ...prev
      ]);
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleSaveCourseDetail = async (id: string, updatedData: any) => {
    try {
      const updated = await apiFetch<Course>(`/api/courses/${id}`, {
        method: 'PUT',
        body: JSON.stringify(updatedData)
      });
      setCourses(prev => prev.map(c => c.id === id ? updated : c));
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleDeleteCourse = async (id: string) => {
    if (!confirm('¿Estás seguro de que deseas eliminar este curso y todas sus clases?')) return;
    try {
      await apiFetch(`/api/courses/${id}`, { method: 'DELETE' });
      setCourses(prev => prev.filter(c => c.id !== id));
      setSelectedCourseId(null);
      setPage('courses');
    } catch (err: any) {
      alert(err.message);
    }
  };

  const toggleTheme = () => {
    const nextDark = !dark;
    setDark(nextDark);
    localStorage.setItem('sh-dark', String(nextDark));
  };

  const handleSelectCourse = (id: string) => {
    setSelectedCourseId(id);
    setPage('course-detail');
  };

  const handleSelectNote = (id: string) => {
    setSelectedNoteId(id);
    setPage('notes');
  };

  if (!token) {
    return <Login done={handleLogin} />;
  }

  return (
    <div className={`app ${dark ? 'dark' : ''}`}>
      <aside className="glass">
        <div className="brand">
          <span className="brandmark">S</span>
          <span>studyhub</span>
        </div>

        <div className="workspace">
          <div className="avatar">
            {profile?.name ? profile.name.charAt(0).toUpperCase() : 'H'}
          </div>
          <div>
            <b>{profile?.name || 'Harold'}</b>
            <small>{profile?.email || 'harold@studyhub.local'}</small>
          </div>
        </div>

        <nav>
          <button className={page === 'dashboard' ? 'active' : ''} onClick={() => setPage('dashboard')}>
            <LayoutDashboard size={18} /> Dashboard
          </button>
          <button className={page === 'courses' || page === 'course-detail' ? 'active' : ''} onClick={() => setPage('courses')}>
            <BookOpen size={18} /> Cursos
          </button>
          <button className={page === 'notes' ? 'active' : ''} onClick={() => setPage('notes')}>
            <FileText size={18} /> Notas
          </button>
          <button className={page === 'calendar' ? 'active' : ''} onClick={() => setPage('calendar')}>
            <CalendarDays size={18} /> Calendario
          </button>
        </nav>

        <div className="navlabel">ORGANIZAR</div>
        <nav>
          <button onClick={() => setSearchOpen(true)}>
            <Search size={18} /> Buscar <kbd>⌘ K</kbd>
          </button>
          <button className={page === 'settings' ? 'active' : ''} onClick={() => setPage('settings')}>
            <Settings size={18} /> Configuración
          </button>
        </nav>

        <div className="sidebottom">
          <button className="theme-toggle" onClick={toggleTheme}>
            {dark ? <Sun size={16} /> : <Moon size={16} />}
            <span>{dark ? 'Tema claro' : 'Tema oscuro'}</span>
          </button>
          <button className="logout-btn" onClick={handleLogout}>
            <LogOut size={16} /> Cerrar sesión
          </button>
          <div className="tip">
            <Sparkles size={15} />
            <span>
              <b>Tu racha va muy bien</b>
              <br />
              Local DB activa
            </span>
          </div>
        </div>
      </aside>

      {/* Mobile Bottom Navigation Bar */}
      <div className="mobile-nav-bar">
        <button className={`mobile-nav-item ${page === 'dashboard' ? 'active' : ''}`} onClick={() => setPage('dashboard')}>
          <LayoutDashboard size={20} />
          <span>Inicio</span>
        </button>
        <button className={`mobile-nav-item ${page === 'courses' || page === 'course-detail' ? 'active' : ''}`} onClick={() => setPage('courses')}>
          <BookOpen size={20} />
          <span>Cursos</span>
        </button>
        <button className={`mobile-nav-item ${page === 'notes' ? 'active' : ''}`} onClick={() => setPage('notes')}>
          <FileText size={20} />
          <span>Notas</span>
        </button>
        <button className={`mobile-nav-item ${page === 'calendar' ? 'active' : ''}`} onClick={() => setPage('calendar')}>
          <CalendarDays size={20} />
          <span>Agenda</span>
        </button>
        <button className={`mobile-nav-item ${page === 'settings' ? 'active' : ''}`} onClick={() => setPage('settings')}>
          <Settings size={20} />
          <span>Ajustes</span>
        </button>
      </div>

      <main>
        <header className="glass">
          <div className="mobilebrand">studyhub</div>
          <div className="search-bar" onClick={() => setSearchOpen(true)}>
            <Search size={18} />
            <span>Buscar en tu espacio...</span>
            <kbd>⌘ K</kbd>
          </div>
          <button className="bell-btn" onClick={() => setPage('settings')}><Bell size={18} /></button>
          <div className="user-avatar" onClick={() => setPage('settings')}>
            {profile?.name ? profile.name.charAt(0).toUpperCase() : 'H'}
          </div>
        </header>

        {page === 'dashboard' && (
          <Dashboard
            courses={courses}
            events={events}
            activityLog={activityLog}
            onSelectCourse={handleSelectCourse}
            onNewCourse={() => setModalOpen(true)}
            notesCount={notes.length}
            setPage={setPage}
            profile={profile}
          />
        )}

        {page === 'courses' && (
          <Courses
            courses={courses}
            onSelectCourse={handleSelectCourse}
            onNewCourse={() => setModalOpen(true)}
          />
        )}

        {page === 'course-detail' && selectedCourseId && (
          <CourseDetail
            courseId={selectedCourseId}
            onBack={() => setPage('courses')}
            onSave={handleSaveCourseDetail}
            onDelete={handleDeleteCourse}
            refreshCourses={fetchData}
          />
        )}

        {page === 'notes' && (
          <Notes
            notes={notes}
            selectedNoteId={selectedNoteId}
            setSelectedNoteId={setSelectedNoteId}
            refreshNotes={fetchData}
          />
        )}

        {page === 'calendar' && (
          <Calendar
            events={events}
            refreshEvents={fetchData}
          />
        )}

        {page === 'settings' && (
          <SettingsPage
            profile={profile}
            setProfile={setProfile}
            courses={courses}
            notes={notes}
            events={events}
          />
        )}
      </main>

      {modalOpen && <NewCourse onClose={() => setModalOpen(false)} onSave={addCourse} />}
      
      <SearchModal
        isOpen={searchOpen}
        onClose={() => setSearchOpen(false)}
        courses={courses}
        notes={notes}
        onSelectCourse={handleSelectCourse}
        onSelectNote={handleSelectNote}
      />
    </div>
  );
}

// ---------------------- SUB-COMPONENTS ----------------------

// 1. DASHBOARD COMPONENT
function Dashboard({
  courses,
  events,
  activityLog,
  onSelectCourse,
  onNewCourse,
  notesCount,
  setPage,
  profile
}: {
  courses: Course[];
  events: CalendarEvent[];
  activityLog: any[];
  onSelectCourse: (id: string) => void;
  onNewCourse: () => void;
  notesCount: number;
  setPage: (p: string) => void;
  profile: UserProfile | null;
}) {
  const totalClasses = useMemo(() => courses.reduce((acc, c) => acc + c.lessons, 0), [courses]);
  const completedClasses = useMemo(() => courses.reduce((acc, c) => acc + c.completed, 0), [courses]);
  
  const upcomingEvents = useMemo(() => {
    return events
      .filter(e => new Date(e.dateTime) >= new Date())
      .slice(0, 3);
  }, [events]);

  const greeting = useMemo(() => {
    const hour = new Date().getHours();
    const name = profile?.name ? `, ${profile.name.split(' ')[0]}` : '';
    if (hour < 12) {
      return `Buenos días${name}`;
    } else if (hour < 19) {
      return `Buenas tardes${name}`;
    } else {
      return `Buenas noches${name}`;
    }
  }, [profile]);

  return (
    <div className="page fade-in">
      <div className="hero">
        <div>
          <p className="eyebrow">{new Date().toLocaleDateString('es-ES', { weekday: 'long', day: 'numeric', month: 'long' }).toUpperCase()}</p>
          <h1>{greeting} <span>✦</span></h1>
          <p className="muted">Tu panel personal de aprendizaje y metas de estudio.</p>
        </div>
        <button className="primary" onClick={onNewCourse}>
          <Plus size={18} /> Nuevo curso
        </button>
      </div>

      <section className="metrics">
        <Metric icon="◈" value={courses.length} label="Cursos activos" tone="purple" />
        <Metric icon="✓" value={`${completedClasses}/${totalClasses}`} label="Clases completadas" tone="green" />
        <Metric icon="▤" value={notesCount} label="Notas guardadas" tone="orange" />
        <Metric icon="◷" value={`${upcomingEvents.length} prox.`} label="Agenda semanal" tone="blue" />
      </section>

      <section className="grid">
        <div className="panel wide glass">
          <div className="panelhead">
            <div>
              <h2>Continúa aprendiendo</h2>
              <p>Retoma donde lo dejaste</p>
            </div>
            <button className="link-btn flex-row" onClick={() => setPage('courses')}>
              Ver todos <ChevronRight size={16} />
            </button>
          </div>

          <div className="course-list">
            {courses.length === 0 ? (
              <div className="empty-panel">
                <BookOpen size={24} className="muted" />
                <p>No tienes cursos activos todavía.</p>
                <button className="outline btn-sm" onClick={onNewCourse}>Crear un curso</button>
              </div>
            ) : (
              courses.slice(0, 3).map(c => (
                <article className="course-row cursor-pointer" key={c.id} onClick={() => onSelectCourse(c.id)}>
                  <div className="course-icon" style={{ background: c.color }}>{c.icon}</div>
                  <div className="grow">
                    <div className="rowtitle">
                      <div>
                        <h3>{c.title}</h3>
                        <p>{c.code} · {c.professor}</p>
                      </div>
                      <button className="iconbtn" onClick={e => { e.stopPropagation(); onSelectCourse(c.id); }}>
                        <ChevronRight size={18} />
                      </button>
                    </div>
                    <div className="progressline">
                      <span style={{ width: `${c.progress}%`, background: c.color }} />
                    </div>
                    <div className="progressmeta">
                      <span>{c.completed} de {c.lessons} clases completadas</span>
                      <b>{c.progress}%</b>
                    </div>
                  </div>
                </article>
              ))
            )}
          </div>
        </div>

        <div className="panel agenda glass">
          <div className="panelhead">
            <div>
              <h2>Próximamente</h2>
              <p>Tu agenda de estudio</p>
            </div>
            <button className="iconbtn" onClick={() => setPage('calendar')}>
              <CalendarDays size={18} />
            </button>
          </div>

          <div className="agenda-events">
            {upcomingEvents.length === 0 ? (
              <div className="empty-panel">
                <Clock size={20} className="muted" />
                <p>No hay eventos próximos.</p>
                <button className="outline btn-sm" onClick={() => setPage('calendar')}>Ver Calendario</button>
              </div>
            ) : (
              upcomingEvents.map(e => {
                const date = new Date(e.dateTime);
                const day = date.getDate();
                const month = date.toLocaleDateString('es-ES', { month: 'short' }).toUpperCase();
                const time = date.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
                return (
                  <div className="event" key={e.id}>
                    <span className="date" style={{ borderColor: e.color, color: e.color }}>
                      {day}
                      <br />
                      <small>{month}</small>
                    </span>
                    <div>
                      <b>{e.title}</b>
                      <p>{e.description || 'Sin descripción'} · {time}</p>
                    </div>
                  </div>
                );
              })
            )}
          </div>
          <button className="fullbutton" onClick={() => setPage('calendar')}>Ver calendario completo</button>
        </div>
      </section>

      <section className="panel activity glass">
        <div className="panelhead">
          <h2>Actividad reciente</h2>
        </div>
        <div className="activity-rows">
          {activityLog.length === 0 ? (
            <p className="muted">Aún no hay actividad registrada.</p>
          ) : (
            activityLog.map(act => (
              <div className="activity-row" key={act.id}>
                <span className="round green">
                  {act.icon === 'BookOpen' ? <BookOpen size={16} /> : act.icon === 'FileText' ? <FileText size={16} /> : <CheckCircle2 size={16} />}
                </span>
                <div>
                  <b>{act.text}</b>
                  <p>{act.time}</p>
                </div>
              </div>
            ))
          )}
        </div>
      </section>
    </div>
  );
}

function Metric({ icon, value, label, tone }: { icon: string; value: string | number; label: string; tone: string }) {
  return (
    <div className="metric glass">
      <span className={`metricicon ${tone}`}>{icon}</span>
      <div>
        <b>{value}</b>
        <p>{label}</p>
      </div>
    </div>
  );
}

// 2. COURSES GRID COMPONENT
function Courses({
  courses,
  onSelectCourse,
  onNewCourse
}: {
  courses: Course[];
  onSelectCourse: (id: string) => void;
  onNewCourse: () => void;
}) {
  const [filter, setFilter] = useState('ALL');
  const [query, setQuery] = useState('');

  const filtered = useMemo(() => {
    return courses.filter(c => {
      const matchQuery = c.title.toLowerCase().includes(query.toLowerCase()) || c.code.toLowerCase().includes(query.toLowerCase());
      if (!matchQuery) return false;
      if (filter === 'ALL') return true;
      return c.status === filter;
    });
  }, [courses, filter, query]);

  return (
    <div className="page fade-in">
      <div className="hero">
        <div>
          <p className="eyebrow">BIBLIOTECA PERSONAL</p>
          <h1>Mis cursos</h1>
          <p className="muted">Organiza cada objetivo de aprendizaje en un solo lugar.</p>
        </div>
        <button className="primary" onClick={onNewCourse}>
          <Plus size={18} /> Nuevo curso
        </button>
      </div>

      <div className="filterbar-container">
        <div className="filterbar">
          <button className={`filter ${filter === 'ALL' ? 'active' : ''}`} onClick={() => setFilter('ALL')}>
            Todos <b>{courses.length}</b>
          </button>
          <button className={`filter ${filter === 'IN_PROGRESS' ? 'active' : ''}`} onClick={() => setFilter('IN_PROGRESS')}>
            En progreso <b>{courses.filter(c => c.status === 'IN_PROGRESS').length}</b>
          </button>
          <button className={`filter ${filter === 'COMPLETED' ? 'active' : ''}`} onClick={() => setFilter('COMPLETED')}>
            Finalizados <b>{courses.filter(c => c.status === 'COMPLETED').length}</b>
          </button>
          <button className={`filter ${filter === 'NOT_STARTED' ? 'active' : ''}`} onClick={() => setFilter('NOT_STARTED')}>
            No iniciados <b>{courses.filter(c => c.status === 'NOT_STARTED').length}</b>
          </button>
        </div>

        <div className="search-inline">
          <Search size={16} />
          <input
            value={query}
            onChange={e => setQuery(e.target.value)}
            placeholder="Filtrar cursos..."
          />
        </div>
      </div>

      <div className="coursegrid">
        {filtered.length === 0 ? (
          <div className="empty-state glass">
            <BookOpen size={48} className="muted" />
            <h3>No se encontraron cursos</h3>
            <p className="muted">Intenta cambiar el filtro o agrega un nuevo curso de estudio.</p>
            <button className="primary" onClick={onNewCourse}><Plus size={18} /> Crear Curso</button>
          </div>
        ) : (
          filtered.map(c => (
            <article className="coursecard glass cursor-pointer" key={c.id} onClick={() => onSelectCourse(c.id)}>
              <div className="cardtop" style={{ background: `linear-gradient(130deg, ${c.color}, ${c.color}bb)` }}>
                <span>{c.icon}</span>
                <span className="card-badge">{c.code}</span>
              </div>
              <div className="cardbody">
                <div className="status">
                  <i style={{ background: c.color }} />
                  {c.status === 'IN_PROGRESS' ? 'En progreso' : c.status === 'COMPLETED' ? 'Completado' : c.status === 'PAUSED' ? 'Pausado' : 'No iniciado'}
                </div>
                <h2>{c.title}</h2>
                <p className="prof">{c.professor} · {c.university || 'Estudio'}</p>
                <div className="cardprogress">
                  <div className="progress-bg">
                    <span style={{ width: `${c.progress}%`, background: c.color }} />
                  </div>
                  <b>{c.progress}%</b>
                </div>
                <p className="lessoncount">
                  <CheckCircle2 size={14} />
                  {c.completed}/{c.lessons} clases completadas
                </p>
              </div>
            </article>
          ))
        )}
      </div>
    </div>
  );
}

// 3. COURSE DETAIL SUB-PAGE (WITH LESSONS CRUD & NOTE EDITING)
function CourseDetail({
  courseId,
  onBack,
  onSave,
  onDelete,
  refreshCourses
}: {
  courseId: string;
  onBack: () => void;
  onSave: (id: string, data: any) => Promise<void>;
  onDelete: (id: string) => Promise<void>;
  refreshCourses: () => void;
}) {
  const [course, setCourse] = useState<Course | null>(null);
  const [lessons, setLessons] = useState<Lesson[]>([]);
  const [newLessonTitle, setNewLessonTitle] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(true);

  // Lesson note editor states
  const [activeLesson, setActiveLesson] = useState<Lesson | null>(null);
  const [lessonNote, setLessonNote] = useState<Note | null>(null);
  const [noteTitle, setNoteTitle] = useState('');
  const [noteBody, setNoteBody] = useState('');
  const [noteSaveStatus, setNoteSaveStatus] = useState<'idle' | 'saving' | 'saved'>('idle');
  const [showEditor, setShowEditor] = useState(false);
  const [showScratchpad, setShowScratchpad] = useState(false);
  const [scratchpadContent, setScratchpadContent] = useState('');
  const [lessonTitleEdit, setLessonTitleEdit] = useState('');
  const [scratchpadEditMode, setScratchpadEditMode] = useState(true);
  const [isMaximized, setIsMaximized] = useState(false);
  const [editorWidthPercent, setEditorWidthPercent] = useState(50);
  const containerRef = useRef<HTMLDivElement>(null);

  const handleDividerMouseDown = (e: React.MouseEvent) => {
    e.preventDefault();
    document.addEventListener('mousemove', handleDividerMouseMove);
    document.addEventListener('mouseup', handleDividerMouseUp);
  };

  const handleDividerMouseMove = (e: MouseEvent) => {
    if (!containerRef.current) return;
    const rect = containerRef.current.getBoundingClientRect();
    const newPercent = ((e.clientX - rect.left) / rect.width) * 100;
    setEditorWidthPercent(Math.max(20, Math.min(80, newPercent)));
  };

  const handleDividerMouseUp = () => {
    document.removeEventListener('mousemove', handleDividerMouseMove);
    document.removeEventListener('mouseup', handleDividerMouseUp);
  };

  const handleDividerTouchStart = (e: React.TouchEvent) => {
    document.addEventListener('touchmove', handleDividerTouchMove, { passive: false });
    document.addEventListener('touchend', handleDividerTouchEnd);
  };

  const handleDividerTouchMove = (e: TouchEvent) => {
    if (!containerRef.current || e.touches.length === 0) return;
    const rect = containerRef.current.getBoundingClientRect();
    const newPercent = ((e.touches[0].clientX - rect.left) / rect.width) * 100;
    setEditorWidthPercent(Math.max(20, Math.min(80, newPercent)));
  };

  const handleDividerTouchEnd = () => {
    document.removeEventListener('touchmove', handleDividerTouchMove);
    document.removeEventListener('touchend', handleDividerTouchEnd);
  };

  // Edit fields state
  const [editTitle, setEditTitle] = useState('');
  const [editCode, setEditCode] = useState('');
  const [editProfessor, setEditProfessor] = useState('');
  const [editUniversity, setEditUniversity] = useState('');
  const [editPlatform, setEditPlatform] = useState('');
  const [editColor, setEditColor] = useState('');
  const [editIcon, setEditIcon] = useState('');
  const [editStatus, setEditStatus] = useState<'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED' | 'PAUSED'>('NOT_STARTED');

  useEffect(() => {
    loadCourseDetails();
  }, [courseId]);

  const loadCourseDetails = async () => {
    try {
      setLoading(true);
      const allCourses = await apiFetch<Course[]>('/api/courses');
      const found = allCourses.find(c => c.id === courseId);
      if (found) {
        setCourse(found);
        setEditTitle(found.title);
        setEditCode(found.code || '');
        setEditProfessor(found.professor || '');
        setEditUniversity(found.university || '');
        setEditPlatform(found.platform || '');
        setEditColor(found.color || '#7257e8');
        setEditIcon(found.icon || '✦');
        setEditStatus(found.status);

        const lessonList = await apiFetch<Lesson[]>(`/api/courses/${courseId}/lessons`);
        setLessons(lessonList);
      }
    } catch (err: any) {
      alert(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleLesson = async (lessonId: string) => {
    try {
      const updatedLesson = await apiFetch<Lesson>(`/api/lessons/${lessonId}/toggle`, { method: 'POST' });
      setLessons(prev => prev.map(l => l.id === lessonId ? updatedLesson : l));
      
      // Update local course stats and parent list
      const updatedCourses = await apiFetch<Course[]>('/api/courses');
      const found = updatedCourses.find(c => c.id === courseId);
      if (found) setCourse(found);
      refreshCourses();
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleOpenLessonNote = async (lesson: Lesson) => {
    setActiveLesson(lesson);
    setNoteSaveStatus('idle');
    try {
      const note = await apiFetch<Note>(`/api/notes/lesson/${lesson.id}`);
      setLessonNote(note);
      setNoteTitle(note.title);
      setNoteBody(note.body);
      setShowEditor(!note.body || note.body.trim() === '');
      setScratchpadContent(note.scratchpad || '');
      setShowScratchpad(false);
      setLessonTitleEdit(lesson.title);
      setScratchpadEditMode(true);
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleSaveLessonNote = async (currentScratchpad = scratchpadContent) => {
    if (!lessonNote) return;
    setNoteSaveStatus('saving');
    try {
      const updated = await apiFetch<Note>(`/api/notes/${lessonNote.id}`, {
        method: 'PUT',
        body: JSON.stringify({
          title: noteTitle,
          body: noteBody,
          lessonId: activeLesson?.id,
          scratchpad: currentScratchpad
        })
      });
      setLessonNote(updated);
      setNoteSaveStatus('saved');
      setTimeout(() => setNoteSaveStatus('idle'), 2000);
    } catch (err: any) {
      alert(err.message);
      setNoteSaveStatus('idle');
    }
  };

  const handleSaveScratchpad = async (content: string) => {
    setScratchpadContent(content);
    if (!lessonNote) return;
    try {
      const updated = await apiFetch<Note>(`/api/notes/${lessonNote.id}`, {
        method: 'PUT',
        body: JSON.stringify({
          title: noteTitle,
          body: noteBody,
          lessonId: activeLesson?.id,
          scratchpad: content
        })
      });
      setLessonNote(updated);
    } catch (err: any) {
      console.error("Error autosaving scratchpad:", err);
    }
  };

  const handleSaveLessonTitle = async () => {
    if (!activeLesson || !lessonTitleEdit.trim() || lessonTitleEdit === activeLesson.title) return;
    try {
      const updated = await apiFetch<Lesson>(`/api/lessons/${activeLesson.id}`, {
        method: 'PUT',
        body: JSON.stringify({ title: lessonTitleEdit.trim(), completed: activeLesson.completed })
      });
      setLessons(prev => prev.map(l => l.id === activeLesson.id ? updated : l));
      setActiveLesson(prev => prev ? { ...prev, title: updated.title } : null);
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleAddLesson = async (e: FormEvent) => {
    e.preventDefault();
    if (!newLessonTitle.trim()) return;
    try {
      const added = await apiFetch<Lesson>(`/api/courses/${courseId}/lessons`, {
        method: 'POST',
        body: JSON.stringify({ title: newLessonTitle.trim(), completed: false })
      });
      setLessons(prev => [...prev, added]);
      setNewLessonTitle('');

      // Refresh course progress stats
      const updatedCourses = await apiFetch<Course[]>('/api/courses');
      const found = updatedCourses.find(c => c.id === courseId);
      if (found) setCourse(found);
      refreshCourses();
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleDeleteLesson = async (lessonId: string) => {
    if (!confirm('¿Eliminar esta clase?')) return;
    try {
      await apiFetch(`/api/lessons/${lessonId}`, { method: 'DELETE' });
      setLessons(prev => prev.filter(l => l.id !== lessonId));

      // Refresh course progress stats
      const updatedCourses = await apiFetch<Course[]>('/api/courses');
      const found = updatedCourses.find(c => c.id === courseId);
      if (found) setCourse(found);
      refreshCourses();
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleSaveChanges = async () => {
    const updatedData = {
      title: editTitle,
      code: editCode,
      professor: editProfessor,
      university: editUniversity,
      platform: editPlatform,
      color: editColor,
      icon: editIcon,
      status: editStatus
    };
    await onSave(courseId, updatedData);
    setIsEditing(false);
    loadCourseDetails();
    refreshCourses();
  };

  if (loading) {
    return <div className="loading-state">Cargando detalles del curso...</div>;
  }

  if (!course) {
    return <div className="error-state">No se pudo encontrar el curso.</div>;
  }

  return (
    <div className="page fade-in">
      <div className="detail-header-nav">
        <button className="outline flex-row" onClick={onBack}>
          <ArrowLeft size={16} /> Volver a cursos
        </button>
        <div className="actions">
          <button className="outline" onClick={() => setIsEditing(!isEditing)}>
            {isEditing ? 'Cancelar' : 'Editar Curso'}
          </button>
          <button className="danger" onClick={() => onDelete(courseId)}>
            <Trash2 size={16} /> Eliminar
          </button>
        </div>
      </div>

      {isEditing ? (
        <div className="panel glass edit-course-panel">
          <h2>Editar Detalles del Curso</h2>
          <div className="edit-grid">
            <label>
              Título del Curso
              <input value={editTitle} onChange={e => setEditTitle(e.target.value)} />
            </label>
            <label>
              Código
              <input value={editCode} onChange={e => setEditCode(e.target.value)} />
            </label>
            <label>
              Profesor
              <input value={editProfessor} onChange={e => setEditProfessor(e.target.value)} />
            </label>
            <label>
              Universidad / Institución
              <input value={editUniversity} onChange={e => setEditUniversity(e.target.value)} />
            </label>
            <label>
              Plataforma
              <input value={editPlatform} onChange={e => setEditPlatform(e.target.value)} />
            </label>
            <label>
              Color (Hex)
              <div className="color-picker-input">
                <input type="color" value={editColor} onChange={e => setEditColor(e.target.value)} />
                <input value={editColor} onChange={e => setEditColor(e.target.value)} />
              </div>
            </label>
            <label>
              Icono (Emoji / Símbolo)
              <input value={editIcon} onChange={e => setEditIcon(e.target.value)} />
            </label>
            <label>
              Estado
              <select value={editStatus} onChange={e => setEditStatus(e.target.value as any)}>
                <option value="NOT_STARTED">No iniciado</option>
                <option value="IN_PROGRESS">En progreso</option>
                <option value="COMPLETED">Completado</option>
                <option value="PAUSED">Pausado</option>
              </select>
            </label>
          </div>
          <button className="primary mt-3 flex-row" onClick={handleSaveChanges}>
            <Save size={16} /> Guardar Cambios
          </button>
        </div>
      ) : (
        <div className="course-detail-summary glass" style={{ borderLeft: `6px solid ${course.color}` }}>
          <div className="details-info">
            <span className="course-emoji" style={{ background: `${course.color}22`, color: course.color }}>
              {course.icon}
            </span>
            <div>
              <span className="code-tag" style={{ background: `${course.color}22`, color: course.color }}>{course.code || 'SIN CÓDIGO'}</span>
              <h1>{course.title}</h1>
              <p className="meta-info">
                <span><b>Profesor:</b> {course.professor || 'Por asignar'}</span>
                <span><b>Universidad:</b> {course.university || 'N/A'}</span>
                <span><b>Plataforma:</b> {course.platform || 'N/A'}</span>
              </p>
            </div>
          </div>

          <div className="progress-section">
            <div className="prog-details">
              <span>Progreso general</span>
              <b>{course.progress}%</b>
            </div>
            <div className="prog-bar-container">
              <div className="prog-bar-fill" style={{ width: `${course.progress}%`, background: course.color }} />
            </div>
            <div className="completed-classes-count">
              {course.completed} de {course.lessons} clases completadas
            </div>
          </div>
        </div>
      )}

      <div className="panel glass mt-4">
        <h2>Clases y Lecciones</h2>
        <p className="muted">Haz clic en cualquier clase para abrir y escribir tus apuntes de estudio.</p>
        
        <form onSubmit={handleAddLesson} className="add-lesson-form">
          <input
            required
            value={newLessonTitle}
            onChange={e => setNewLessonTitle(e.target.value)}
            placeholder="Ej. Clase 03: Introducción a Patrones de Diseño"
          />
          <button className="primary" type="submit">
            <Plus size={16} /> Agregar Clase
          </button>
        </form>

        <div className="lessons-list">
          {lessons.length === 0 ? (
            <div className="empty-lessons">
              <CheckCircle2 size={32} className="muted" />
              <p>No has agregado clases a este curso todavía.</p>
            </div>
          ) : (
            lessons.map(l => (
              <div 
                className={`lesson-row cursor-pointer ${l.completed ? 'completed' : ''}`} 
                key={l.id} 
                onClick={() => handleOpenLessonNote(l)}
                style={l.completed ? {
                  background: `${course.color}15`,
                  borderColor: `${course.color}60`
                } : {}}
              >
                <div className="lesson-left">
                  <span className={`checkbox ${l.completed ? 'checked' : ''}`} style={{ borderColor: course.color, background: l.completed ? course.color : 'transparent' }} onClick={e => { e.stopPropagation(); handleToggleLesson(l.id); }}>
                    {l.completed && <Check size={12} style={{ color: '#fff' }} />}
                  </span>
                  <span className="lesson-title">{l.title}</span>
                </div>
                <button className="delete-lesson-btn" onClick={e => { e.stopPropagation(); handleDeleteLesson(l.id); }}>
                  <Trash2 size={16} />
                </button>
              </div>
            ))
          )}
        </div>
      </div>

      {activeLesson && lessonNote && (
        <div className="overlay" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '20px', padding: isMaximized ? '0' : '20px' }} onClick={() => { handleSaveLessonNote(); setActiveLesson(null); setIsMaximized(false); }}>
          <div className="modal glass" style={{ width: isMaximized ? '100vw' : (showScratchpad ? 'min(700px, 60vw)' : 'min(1000px, 95vw)'), height: isMaximized ? '100vh' : '90vh', maxWidth: 'none', borderRadius: isMaximized ? '0' : '16px', display: 'flex', flexDirection: 'column', transition: 'all 0.3s ease' }} onClick={e => e.stopPropagation()}>
            <div className="modalhead" style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '16px', marginBottom: '8px' }}>
              <div>
                <span className="code-tag" style={{ background: `${course.color}22`, color: course.color, marginBottom: '4px' }}>{course.title}</span>
                <input
                  value={lessonTitleEdit}
                  onChange={e => setLessonTitleEdit(e.target.value)}
                  style={{
                    fontSize: '18px',
                    fontWeight: 700,
                    background: 'transparent',
                    border: '1px solid transparent',
                    color: 'var(--text-main)',
                    width: '100%',
                    padding: '2px 4px',
                    borderRadius: '6px',
                    outline: 'none',
                    transition: 'all 0.2s',
                    marginTop: '2px'
                  }}
                  onFocus={e => e.target.style.borderColor = 'var(--border-color)'}
                  onBlur={e => {
                    e.target.style.borderColor = 'transparent';
                    handleSaveLessonTitle();
                  }}
                  placeholder="Título de la clase..."
                  title="Haz clic para editar el nombre de la clase"
                />
                <p className="muted" style={{ fontSize: '12px' }}>Toma apuntes y notas de la clase en formato Markdown.</p>
              </div>
              <div className="flex-row" style={{ gap: '12px' }}>
                <button className="outline flex-row" onClick={() => handleToggleLesson(activeLesson.id).then(() => {
                  setActiveLesson(prev => prev ? { ...prev, completed: !prev.completed } : null);
                })}>
                  <CheckCircle2 size={16} /> {activeLesson.completed ? 'Completada' : 'Marcar Completada'}
                </button>
                <button className="outline flex-row" onClick={() => setIsMaximized(!isMaximized)} title={isMaximized ? "Minimizar" : "Maximizar"}>
                  {isMaximized ? <Minimize2 size={16} /> : <Maximize2 size={16} />}
                </button>
                <button type="button" onClick={() => { handleSaveLessonNote(); setActiveLesson(null); setIsMaximized(false); }}><X size={20} /></button>
              </div>
            </div>

            <div className="note-editor-container" style={{ flexGrow: 1, display: 'flex', flexDirection: 'column', height: 'calc(100% - 90px)', minHeight: 0 }}>
              <div className="editor-header" style={{ padding: '8px 0', borderBottom: '1px solid var(--border-color)', marginBottom: '8px' }}>
                <input
                  className="note-title-input"
                  value={noteTitle}
                  onChange={e => setNoteTitle(e.target.value)}
                  placeholder="Título de la nota..."
                  style={{ fontSize: '16px' }}
                />
                <div className="editor-actions">
                  <span className="save-indicator">
                    {noteSaveStatus === 'saving' && 'Guardando...'}
                    {noteSaveStatus === 'saved' && <><Check size={14} /> Guardado</>}
                  </span>
                  <button className={`outline flex-row ${showScratchpad ? 'active' : ''}`} onClick={() => {
                    const next = !showScratchpad;
                    setShowScratchpad(next);
                    if (next) setScratchpadEditMode(true);
                  }}>
                    <FileText size={16} /> {showScratchpad ? 'Ocultar Apuntes' : 'Apuntes Rápidos'}
                  </button>
                  <button className="outline flex-row" onClick={() => setShowEditor(!showEditor)}>
                    <Edit size={16} /> {showEditor ? 'Ocultar Editor' : 'Editor Markdown'}
                  </button>
                  <button className="outline flex-row" onClick={() => handleSaveLessonNote()}>
                    <Save size={16} /> Guardar
                  </button>
                </div>
              </div>

              <div className="note-columns" ref={containerRef} style={{ flexGrow: 1, display: 'flex', minHeight: 0, position: 'relative' }}>
                {showEditor && (
                  <div className="note-column-edit" style={{ width: `${editorWidthPercent}%`, display: 'flex', flexDirection: 'column' }}>
                    <div className="column-label">EDITOR MARKDOWN</div>
                    <textarea
                      value={noteBody}
                      onChange={e => setNoteBody(e.target.value)}
                      placeholder="Escribe tus resúmenes de clase usando Markdown..."
                      style={{ flexGrow: 1, border: 0, resize: 'none', outline: 'none', padding: '12px', background: 'transparent', color: 'var(--text-main)', fontFamily: 'monospace' }}
                    />
                  </div>
                )}

                {showEditor && (
                  <div
                    className="notes-divider"
                    onMouseDown={handleDividerMouseDown}
                    onTouchStart={handleDividerTouchStart}
                    style={{
                      width: '6px',
                      cursor: 'col-resize',
                      background: 'var(--border-color)',
                      transition: 'background 0.2s',
                      userSelect: 'none',
                      flexShrink: 0
                    }}
                    onMouseEnter={e => e.currentTarget.style.background = 'var(--primary-color)'}
                    onMouseLeave={e => e.currentTarget.style.background = 'var(--border-color)'}
                  />
                )}

                <div className="note-column-preview" style={{ width: showEditor ? `${100 - editorWidthPercent}%` : '100%', display: 'flex', flexDirection: 'column', overflowY: 'auto' }}>
                  <div className="column-label">VISTA PREVIA</div>
                  <div className="markdown-body" style={{ padding: '12px', flexGrow: 1 }}>
                    {parseMarkdown(noteBody)}
                  </div>
                </div>
              </div>
            </div>
          </div>

          {showScratchpad && (
            <div className="scratchpad-panel glass" style={{ width: '350px', height: '90vh', display: 'flex', flexDirection: 'column', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)', animation: 'slideInRight 0.3s ease' }} onClick={e => e.stopPropagation()}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px', borderBottom: '1px solid var(--border-color)', paddingBottom: '8px' }}>
                <b style={{ color: '#ffffff', fontSize: '15px', display: 'flex', alignItems: 'center', gap: '6px' }}>📌 Apuntes Rápidos</b>
                <button type="button" onClick={() => setShowScratchpad(false)} style={{ padding: '4px', minWidth: 'auto', background: 'transparent' }}><X size={16} /></button>
              </div>

              {scratchpadEditMode ? (
                <textarea
                  value={scratchpadContent}
                  onChange={e => handleSaveScratchpad(e.target.value)}
                  placeholder="Escribe aquí tus notas rápidas en Markdown..."
                  style={{ flexGrow: 1, border: 0, resize: 'none', outline: 'none', background: 'transparent', color: '#ffffff', fontSize: '13.5px', lineHeight: '1.6', fontFamily: 'monospace' }}
                />
              ) : (
                <div className="markdown-body" style={{ flexGrow: 1, overflowY: 'auto', paddingRight: '4px' }}>
                  {parseMarkdown(scratchpadContent)}
                </div>
              )}

              <div style={{ borderTop: '1px solid var(--border-color)', paddingTop: '12px', marginTop: '8px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span style={{ fontSize: '11px', color: 'rgba(255, 255, 255, 0.4)' }}>
                  {scratchpadEditMode ? 'Se guarda automáticamente' : 'Vista Previa'}
                </span>
                <div style={{ display: 'flex', gap: '8px' }}>
                  {!scratchpadEditMode && (
                    <button className="outline flex-row" style={{ padding: '6px 12px', fontSize: '12px', minWidth: 'auto' }} onClick={() => setScratchpadEditMode(true)}>
                      <Edit size={12} /> Editar
                    </button>
                  )}
                  {scratchpadEditMode && (
                    <button className="primary flex-row" style={{ padding: '6px 12px', fontSize: '12px', minWidth: 'auto' }} onClick={() => {
                      handleSaveLessonNote();
                      setScratchpadEditMode(false);
                    }}>
                      Guardar
                    </button>
                  )}
                </div>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

// 4. NOTES & MARKDOWN EDITOR COMPONENT
function Notes({
  notes,
  selectedNoteId,
  setSelectedNoteId,
  refreshNotes
}: {
  notes: Note[];
  selectedNoteId: string | null;
  setSelectedNoteId: (id: string | null) => void;
  refreshNotes: () => void;
}) {
  const [activeNote, setActiveNote] = useState<Note | null>(null);
  const [noteTitle, setNoteTitle] = useState('');
  const [noteBody, setNoteBody] = useState('');
  const [query, setQuery] = useState('');
  const [saveStatus, setSaveStatus] = useState<'idle' | 'saving' | 'saved'>('idle');
  const [showEditor, setShowEditor] = useState(true);

  useEffect(() => {
    if (selectedNoteId) {
      const note = notes.find(n => n.id === selectedNoteId);
      if (note) {
        setActiveNote(note);
        setNoteTitle(note.title);
        setNoteBody(note.body);
      }
    } else if (notes.length > 0) {
      setActiveNote(notes[0]);
      setNoteTitle(notes[0].title);
      setNoteBody(notes[0].body);
      setSelectedNoteId(notes[0].id);
    } else {
      setActiveNote(null);
      setNoteTitle('');
      setNoteBody('');
    }
  }, [selectedNoteId, notes]);

  const filteredNotes = useMemo(() => {
    return notes.filter(n => n.title.toLowerCase().includes(query.toLowerCase()) || n.body.toLowerCase().includes(query.toLowerCase()));
  }, [notes, query]);

  const handleCreateNote = async () => {
    try {
      const added = await apiFetch<Note>('/api/notes', {
        method: 'POST',
        body: JSON.stringify({ title: 'Nueva Nota', body: '' })
      });
      refreshNotes();
      setSelectedNoteId(added.id);
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleSaveNote = async () => {
    if (!activeNote) return;
    setSaveStatus('saving');
    try {
      const updated = await apiFetch<Note>(`/api/notes/${activeNote.id}`, {
        method: 'PUT',
        body: JSON.stringify({ title: noteTitle, body: noteBody })
      });
      setSaveStatus('saved');
      refreshNotes();
      setTimeout(() => setSaveStatus('idle'), 2000);
    } catch (err: any) {
      alert(err.message);
      setSaveStatus('idle');
    }
  };

  const handleDeleteNote = async () => {
    if (!activeNote || !confirm('¿Estás seguro de que deseas eliminar esta nota?')) return;
    try {
      await apiFetch(`/api/notes/${activeNote.id}`, { method: 'DELETE' });
      setSelectedNoteId(null);
      refreshNotes();
    } catch (err: any) {
      alert(err.message);
    }
  };

  return (
    <div className="page fade-in notes-page">
      <div className="notes-sidebar glass">
        <div className="sidebar-header">
          <button className="primary w-full" onClick={handleCreateNote}>
            <Plus size={16} /> Nueva Nota
          </button>
          <div className="search-inline mt-2">
            <Search size={16} />
            <input value={query} onChange={e => setQuery(e.target.value)} placeholder="Buscar notas..." />
          </div>
        </div>
        <div className="sidebar-notes-list">
          {filteredNotes.map(n => (
            <div
              className={`sidebar-note-item ${activeNote?.id === n.id ? 'active' : ''}`}
              key={n.id}
              onClick={() => setSelectedNoteId(n.id)}
            >
              <b>{n.title || 'Nota sin título'}</b>
              <p>{n.body ? n.body.substring(0, 45) + '...' : 'Nota vacía'}</p>
              <small>{new Date(n.lastModified).toLocaleDateString('es-ES')}</small>
            </div>
          ))}
        </div>
      </div>

      <div className="notes-workspace glass">
        {activeNote ? (
          <div className="note-editor-container">
            <div className="editor-header">
              <input
                className="note-title-input"
                value={noteTitle}
                onChange={e => setNoteTitle(e.target.value)}
                placeholder="Título de la nota..."
              />
              <div className="editor-actions">
                <span className="save-indicator">
                  {saveStatus === 'saving' && 'Guardando...'}
                  {saveStatus === 'saved' && <><Check size={14} /> Guardado</>}
                </span>
                <button className="outline flex-row" onClick={() => setShowEditor(!showEditor)}>
                  <Edit size={16} /> {showEditor ? 'Ocultar Editor' : 'Editor Markdown'}
                </button>
                <button className="outline flex-row" onClick={handleSaveNote}>
                  <Save size={16} /> Guardar
                </button>
                <button className="danger" onClick={handleDeleteNote}>
                  <Trash2 size={16} />
                </button>
              </div>
            </div>

            <div className="note-columns">
              {showEditor && (
                <div className="note-column-edit">
                  <div className="column-label">EDITOR MARKDOWN</div>
                  <textarea
                    value={noteBody}
                    onChange={e => setNoteBody(e.target.value)}
                    placeholder="Escribe en formato Markdown... 
# Encabezado 1
## Encabezado 2
- Elemento de lista
> Cita textual"
                  />
                </div>
              )}

              <div className="note-column-preview" style={{ width: showEditor ? '50%' : '100%' }}>
                <div className="column-label">VISTA PREVIA</div>
                <div className="markdown-body">
                  {parseMarkdown(noteBody)}
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="empty-notes-workspace">
            <FileText size={48} className="muted" />
            <h3>No hay nota seleccionada</h3>
            <p className="muted">Selecciona una nota de la izquierda o crea una nueva para empezar a escribir.</p>
            <button className="primary" onClick={handleCreateNote}><Plus size={16} /> Crear Nota</button>
          </div>
        )}
      </div>
    </div>
  );
}

// 5. DYNAMIC CALENDAR COMPONENT
function Calendar({
  events,
  refreshEvents
}: {
  events: CalendarEvent[];
  refreshEvents: () => void;
}) {
  const [currentDate, setCurrentDate] = useState(new Date(2026, 6, 1)); // Start on July 2026 for consistency
  const [modalOpen, setModalOpen] = useState(false);
  const [eventTitle, setEventTitle] = useState('');
  const [eventDesc, setEventDesc] = useState('');
  const [eventDate, setEventDate] = useState('');
  const [eventTime, setEventTime] = useState('10:00');
  const [eventColor, setEventColor] = useState('#7257e8');
  const [selectedDay, setSelectedDay] = useState<number | null>(null);

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();

  const monthName = currentDate.toLocaleDateString('es-ES', { month: 'long', year: 'numeric' });

  // Calculation for calendar grid
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const startDayIndex = (new Date(year, month, 1).getDay() + 6) % 7; // Align to Monday start

  const calendarDays = useMemo(() => {
    const list = [];
    // Padding days from previous month
    for (let i = 0; i < startDayIndex; i++) {
      list.push(null);
    }
    // Days of current month
    for (let i = 1; i <= daysInMonth; i++) {
      list.push(i);
    }
    return list;
  }, [year, month, daysInMonth, startDayIndex]);

  const handlePrevMonth = () => {
    setCurrentDate(new Date(year, month - 1, 1));
  };

  const handleNextMonth = () => {
    setCurrentDate(new Date(year, month + 1, 1));
  };

  const getEventsForDay = (day: number) => {
    return events.filter(e => {
      const d = new Date(e.dateTime);
      return d.getFullYear() === year && d.getMonth() === month && d.getDate() === day;
    });
  };

  const getEventTimeRange = (e: CalendarEvent) => {
    const startDate = new Date(e.dateTime);
    const startStr = startDate.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit', hour12: false });
    
    const titleLower = e.title.toLowerCase();
    if (titleLower.includes("data engineer")) {
      return `${startStr} - 10:30`;
    } else if (titleLower.includes("java backend") || titleLower.includes("java")) {
      return `${startStr} - 13:00`;
    } else if (titleLower.includes("inglés") || titleLower.includes("ingles")) {
      return `${startStr} - 20:00`;
    }
    
    const endDate = new Date(startDate.getTime() + 60 * 60 * 1000);
    const endStr = endDate.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit', hour12: false });
    return `${startStr} - ${endStr}`;
  };

  const handleAddEventSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!eventTitle.trim() || !eventDate) return;
    try {
      const dateTime = `${eventDate}T${eventTime}:00`;
      await apiFetch('/api/events', {
        method: 'POST',
        body: JSON.stringify({ title: eventTitle, description: eventDesc, dateTime, color: eventColor })
      });
      setModalOpen(false);
      setEventTitle('');
      setEventDesc('');
      refreshEvents();
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleDeleteEvent = async (id: string, e: React.MouseEvent) => {
    e.stopPropagation();
    if (!confirm('¿Eliminar este evento de la agenda?')) return;
    try {
      await apiFetch(`/api/events/${id}`, { method: 'DELETE' });
      refreshEvents();
    } catch (err: any) {
      alert(err.message);
    }
  };

  return (
    <div className="page fade-in">
      <div className="hero">
        <div>
          <p className="eyebrow">PLANIFICACIÓN</p>
          <h1>Calendario</h1>
          <p className="muted">Organiza tus clases, entregas y recordatorios.</p>
        </div>
        <button className="primary" onClick={() => setModalOpen(true)}>
          <Plus size={18} /> Agregar evento
        </button>
      </div>

      <div className="calendar glass">
        <div className="calhead">
          <b className="capitalize-first">{monthName}</b>
          <div className="cal-nav-buttons">
            <button className="nav-arrow" onClick={handlePrevMonth}>‹</button>
            <button className="nav-today" onClick={() => setCurrentDate(new Date())}>Hoy</button>
            <button className="nav-arrow" onClick={handleNextMonth}>›</button>
          </div>
        </div>

        <div className="week">
          {['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'].map(x => (
            <b key={x}>{x}</b>
          ))}
        </div>

        <div className="days">
          {calendarDays.map((day, i) => {
            const isToday = day !== null &&
              new Date().getDate() === day &&
              new Date().getMonth() === month &&
              new Date().getFullYear() === year;

            const dayEvents = day ? getEventsForDay(day) : [];

            return (
              <div 
                className={`cal-day-cell cursor-pointer ${day === null ? 'empty-day' : ''} ${isToday ? 'today' : ''}`} 
                key={i}
                onClick={() => { if (day !== null) setSelectedDay(day); }}
              >
                {day && (
                  <>
                    <b className="day-number">{day}</b>
                    <div className="cell-events">
                      {dayEvents.map(e => (
                        <span
                          key={e.id}
                          className="cal-event"
                          style={{ background: `${e.color}18`, color: e.color, borderLeft: `3px solid ${e.color}` }}
                          onClick={ev => {
                            ev.stopPropagation();
                            setSelectedDay(day);
                          }}
                        >
                          {e.title}
                        </span>
                      ))}
                    </div>
                  </>
                )}
              </div>
            );
          })}
        </div>
      </div>

      {/* Daily Details Modal */}
      {selectedDay !== null && (
        <div className="overlay" onClick={() => setSelectedDay(null)}>
          <div className="modal glass" style={{ width: 'min(500px, 95vw)', display: 'flex', flexDirection: 'column' }} onClick={e => e.stopPropagation()}>
            <div className="modalhead" style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '12px', marginBottom: '16px' }}>
              <div>
                <h2>Horario del Día</h2>
                <p className="muted" style={{ fontSize: '13px', textTransform: 'capitalize' }}>
                  {selectedDay} de {currentDate.toLocaleDateString('es-ES', { month: 'long', year: 'numeric' })}
                </p>
              </div>
              <button type="button" onClick={() => setSelectedDay(null)}><X size={20} /></button>
            </div>

            <div className="day-events-list">
              {getEventsForDay(selectedDay).length === 0 ? (
                <p className="muted" style={{ textAlign: 'center', padding: '24px 0' }}>No hay eventos programados para este día.</p>
              ) : (
                getEventsForDay(selectedDay)
                  .sort((a, b) => a.dateTime.localeCompare(b.dateTime))
                  .map(e => {
                    const timeRange = getEventTimeRange(e);
                    return (
                      <div key={e.id} className="day-event-item" style={{ borderLeft: `4px solid ${e.color}` }}>
                        <div className="day-event-details">
                          <div className="day-event-header">
                            <span className="day-event-time" style={{ background: e.color, color: '#ffffff' }}>{timeRange}</span>
                            <b className="day-event-title">{e.title}</b>
                          </div>
                          {e.description && <p className="day-event-desc">{e.description}</p>}
                        </div>
                        <button className="delete-lesson-btn" style={{ padding: '6px', minWidth: 'auto', background: 'transparent' }} onClick={ev => { ev.stopPropagation(); handleDeleteEvent(e.id, ev).then(() => {
                          // Close modal if no more events
                          if (getEventsForDay(selectedDay).length <= 1) {
                            setSelectedDay(null);
                          }
                        }); }}>
                          <Trash2 size={16} />
                        </button>
                      </div>
                    );
                  })
              )}
            </div>

            <div className="modalactions" style={{ marginTop: '20px', borderTop: '1px solid var(--border-color)', paddingTop: '12px' }}>
              <button className="primary flex-row" onClick={() => {
                setSelectedDay(null);
                const monthStr = String(month + 1).padStart(2, '0');
                const dayStr = String(selectedDay).padStart(2, '0');
                setEventDate(`${year}-${monthStr}-${dayStr}`);
                setModalOpen(true);
              }}>
                <Plus size={16} /> Agregar evento
              </button>
              <button type="button" className="outline" onClick={() => setSelectedDay(null)}>Cerrar</button>
            </div>
          </div>
        </div>
      )}

      {modalOpen && (
        <div className="overlay">
          <form className="modal glass" onSubmit={handleAddEventSubmit}>
            <div className="modalhead">
              <div>
                <h2>Agregar Evento</h2>
                <p>Planifica tu próxima tarea o sesión de estudio.</p>
              </div>
              <button type="button" onClick={() => setModalOpen(false)}><X size={20} /></button>
            </div>

            <label>
              Título del evento
              <input required value={eventTitle} onChange={e => setEventTitle(e.target.value)} placeholder="Ej. Repaso General de Patrones" />
            </label>

            <label>
              Descripción
              <input value={eventDesc} onChange={e => setEventDesc(e.target.value)} placeholder="Detalles de la sesión..." />
            </label>

            <div className="twocol">
              <label>
                Fecha
                <input required type="date" value={eventDate} onChange={e => setEventDate(e.target.value)} />
              </label>
              <label>
                Hora
                <input required type="time" value={eventTime} onChange={e => setEventTime(e.target.value)} />
              </label>
            </div>

            <label>
              Color del evento
              <div className="color-picker-input">
                <input type="color" value={eventColor} onChange={e => setEventColor(e.target.value)} />
                <input value={eventColor} onChange={e => setEventColor(e.target.value)} />
              </div>
            </label>

            <div className="modalactions">
              <button type="button" className="outline" onClick={() => setModalOpen(false)}>Cancelar</button>
              <button className="primary">Guardar Evento</button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}

// 6. SETTINGS PAGE COMPONENT
function SettingsPage({
  profile,
  setProfile,
  courses,
  notes,
  events
}: {
  profile: UserProfile | null;
  setProfile: (p: UserProfile) => void;
  courses: Course[];
  notes: Note[];
  events: CalendarEvent[];
}) {
  const [name, setName] = useState(profile?.name || '');
  const [email, setEmail] = useState(profile?.email || '');
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  
  const [profileMsg, setProfileMsg] = useState('');
  const [passMsg, setPassMsg] = useState('');

  useEffect(() => {
    if (profile) {
      setName(profile.name);
      setEmail(profile.email);
    }
  }, [profile]);

  const handleUpdateProfile = async (e: FormEvent) => {
    e.preventDefault();
    setProfileMsg('');
    try {
      const updated = await apiFetch<UserProfile>('/api/auth/profile', {
        method: 'PUT',
        body: JSON.stringify({ name, email })
      });
      setProfile(updated);
      setProfileMsg('Perfil actualizado correctamente');
    } catch (err: any) {
      setProfileMsg(`Error: ${err.message}`);
    }
  };

  const handleChangePassword = async (e: FormEvent) => {
    e.preventDefault();
    setPassMsg('');
    try {
      await apiFetch('/api/auth/password', {
        method: 'PUT',
        body: JSON.stringify({ oldPassword, newPassword })
      });
      setOldPassword('');
      setNewPassword('');
      setPassMsg('Contraseña actualizada con éxito');
    } catch (err: any) {
      setPassMsg(`Error: ${err.message}`);
    }
  };

  const handleExportData = () => {
    const dataStr = JSON.stringify({ courses, notes, events }, null, 2);
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);
    
    const exportFileDefaultName = 'studyhub_backup.json';
    
    const linkElement = document.createElement('a');
    linkElement.setAttribute('href', dataUri);
    linkElement.setAttribute('download', exportFileDefaultName);
    linkElement.click();
  };

  return (
    <div className="page fade-in">
      <div className="hero">
        <div>
          <p className="eyebrow">PREFERENCIAS</p>
          <h1>Configuración</h1>
          <p className="muted">Administra tus datos y las preferencias del espacio de estudio.</p>
        </div>
      </div>

      <div className="settings-grid">
        <section className="panel glass">
          <h2>Editar Perfil</h2>
          <form onSubmit={handleUpdateProfile}>
            <label>
              Nombre Completo
              <input value={name} onChange={e => setName(e.target.value)} required />
            </label>
            <label>
              Email / Usuario
              <input value={email} onChange={e => setEmail(e.target.value)} required type="email" />
            </label>
            {profileMsg && <div className="info-message">{profileMsg}</div>}
            <button className="primary" type="submit">Actualizar Perfil</button>
          </form>
        </section>

        <section className="panel glass">
          <h2>Cambiar Contraseña</h2>
          <form onSubmit={handleChangePassword}>
            <label>
              Contraseña anterior
              <input type="password" value={oldPassword} onChange={e => setOldPassword(e.target.value)} required />
            </label>
            <label>
              Nueva contraseña (mínimo 6 caracteres)
              <input type="password" value={newPassword} onChange={e => setNewPassword(e.target.value)} required minLength={6} />
            </label>
            {passMsg && <div className="info-message">{passMsg}</div>}
            <button className="primary" type="submit">Cambiar Contraseña</button>
          </form>
        </section>

        <section className="panel glass full-width">
          <h2>Seguridad y Datos</h2>
          <p className="muted">Puedes descargar toda tu información incluyendo cursos creados, notas tomadas y eventos programados.</p>
          <div className="button-group mt-3">
            <button className="outline flex-row" onClick={handleExportData}>
              Exportar mi información (JSON)
            </button>
          </div>
        </section>
      </div>
    </div>
  );
}

// 7. NEW COURSE DIALOG MODAL
function NewCourse({
  onClose,
  onSave
}: {
  onClose: () => void;
  onSave: (f: FormData) => void;
}) {
  const [courseColor, setCourseColor] = useState('#7257e8');

  return (
    <div className="overlay">
      <form className="modal glass" onSubmit={event => { event.preventDefault(); onSave(new FormData(event.currentTarget)); }}>
        <div className="modalhead">
          <div>
            <h2>Crear Curso</h2>
            <p>Empieza a organizar un nuevo objetivo de aprendizaje.</p>
          </div>
          <button type="button" onClick={onClose}><X size={20} /></button>
        </div>

        <label>
          Nombre del curso
          <input required name="title" placeholder="Ej. Arquitectura de Software" autoFocus />
        </label>

        <div className="twocol">
          <label>
            Código / Sigla
            <input name="code" placeholder="ARQ-401" />
          </label>
          <label>
            Profesor / Instructor
            <input name="professor" placeholder="Dra. Ana Torres" />
          </label>
        </div>

        <div className="twocol">
          <label>
            Universidad / Escuela
            <input name="university" placeholder="Ej. Universidad Central" />
          </label>
          <label>
            Plataforma / Enlace
            <input name="platform" placeholder="Ej. EdX o Aula Virtual" />
          </label>
        </div>

        <div className="twocol">
          <label>
            Color distintivo
            <div className="color-picker-input">
              <input type="color" name="color" value={courseColor} onChange={e => setCourseColor(e.target.value)} />
              <input value={courseColor} onChange={e => setCourseColor(e.target.value)} />
            </div>
          </label>
          <label>
            Icono representativo (Emoji o Letra)
            <input name="icon" defaultValue="✦" placeholder="Ej. ◈, ⌘, ▦ o un emoji" maxLength={2} />
          </label>
        </div>

        <div className="modalactions">
          <button type="button" className="outline" onClick={onClose}>Cancelar</button>
          <button className="primary" type="submit">Crear Curso</button>
        </div>
      </form>
    </div>
  );
}

// Render root component
createRoot(document.getElementById('root')!).render(<App />);
