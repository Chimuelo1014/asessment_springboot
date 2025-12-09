// src/components/dashboard/Dashboard.jsx
import { useState, useEffect } from 'react';
import { FileText, User, LogOut } from 'lucide-react';
import api from '../../services/api';
import ApplicationsTable from './ApplicationsTable';
import AffiliatesGrid from './AffiliatesGrid';
import CreateApplicationModal from './CreateApplicationModal';
import CreateAffiliateModal from './CreateAffiliateModal';

const Dashboard = ({ user, onLogout }) => {
  const [activeTab, setActiveTab] = useState('applications');
  const [applications, setApplications] = useState([]);
  const [affiliates, setAffiliates] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showCreateAppModal, setShowCreateAppModal] = useState(false);
  const [showCreateAffModal, setShowCreateAffModal] = useState(false);

  const isAdmin = user.role === 'ROLE_ADMIN';
  const isAnalyst = user.role === 'ROLE_ANALISTA' || isAdmin;
  const isAffiliate = user.role === 'ROLE_AFILIADO';

  useEffect(() => {
    loadData();
  }, [activeTab]);

  const loadData = async () => {
    setLoading(true);
    try {
      if (activeTab === 'applications') {
        if (isAnalyst) {
          const response = await api.applications.getAll();
          setApplications(response.data);
        }
      } else if (activeTab === 'affiliates' && isAnalyst) {
        const response = await api.affiliates.getAll();
        setAffiliates(response.data);
      }
    } catch (err) {
      console.error('Error loading data:', err);
      alert('Error loading data: ' + (err.response?.data?.detail || err.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-3">
              <div className="bg-indigo-600 w-10 h-10 rounded-lg flex items-center justify-center">
                <FileText className="text-white" size={24} />
              </div>
              <div>
                <h1 className="text-2xl font-bold text-gray-800">CoopCredit</h1>
                <p className="text-sm text-gray-600">{user.role.replace('ROLE_', '')}</p>
              </div>
            </div>

            <div className="flex items-center gap-4">
              <div className="flex items-center gap-2 text-gray-700">
                <User size={20} />
                <span className="font-medium">{user.username}</span>
              </div>
              <button
                onClick={onLogout}
                className="flex items-center gap-2 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition"
              >
                <LogOut size={20} />
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation */}
      <nav className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex gap-8">
            <button
              onClick={() => setActiveTab('applications')}
              className={`py-4 border-b-2 font-medium transition ${
                activeTab === 'applications'
                  ? 'border-indigo-600 text-indigo-600'
                  : 'border-transparent text-gray-600 hover:text-gray-800'
              }`}
            >
              Credit Applications
            </button>
            {isAnalyst && (
              <button
                onClick={() => setActiveTab('affiliates')}
                className={`py-4 border-b-2 font-medium transition ${
                  activeTab === 'affiliates'
                    ? 'border-indigo-600 text-indigo-600'
                    : 'border-transparent text-gray-600 hover:text-gray-800'
                }`}
              >
                Affiliates
              </button>
            )}
          </div>
        </div>
      </nav>

      {/* Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {loading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Loading...</p>
          </div>
        ) : activeTab === 'applications' ? (
          <ApplicationsTable
            applications={applications}
            onRefresh={loadData}
            isAffiliate={isAffiliate}
            isAnalyst={isAnalyst}
            onCreateNew={() => setShowCreateAppModal(true)}
          />
        ) : (
          <AffiliatesGrid
            affiliates={affiliates}
            onRefresh={loadData}
            onCreateNew={() => setShowCreateAffModal(true)}
          />
        )}
      </main>

      {/* Modals */}
      {showCreateAppModal && (
        <CreateApplicationModal
          onClose={() => setShowCreateAppModal(false)}
          onSuccess={() => {
            setShowCreateAppModal(false);
            loadData();
          }}
        />
      )}

      {showCreateAffModal && (
        <CreateAffiliateModal
          onClose={() => setShowCreateAffModal(false)}
          onSuccess={() => {
            setShowCreateAffModal(false);
            loadData();
          }}
        />
      )}
    </div>
  );
};

export default Dashboard;