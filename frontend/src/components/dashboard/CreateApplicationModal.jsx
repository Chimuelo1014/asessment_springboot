import { useState } from 'react';
import { AlertCircle } from 'lucide-react';
import api from '../../services/api';

const CreateApplicationModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    affiliateId: '',
    requestedAmount: '',
    termMonths: '',
    interestRate: '12.5',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await api.applications.create({
        affiliateId: parseInt(formData.affiliateId),
        requestedAmount: parseFloat(formData.requestedAmount),
        termMonths: parseInt(formData.termMonths),
        interestRate: parseFloat(formData.interestRate),
      });
      alert('Application created successfully!');
      onSuccess();
    } catch (err) {
      setError(err.response?.data?.detail || err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg max-w-md w-full p-6">
        <h2 className="text-2xl font-bold mb-4">New Credit Application</h2>
        
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">Affiliate ID</label>
            <input
              type="number"
              value={formData.affiliateId}
              onChange={(e) => setFormData({ ...formData, affiliateId: e.target.value })}
              className="w-full px-3 py-2 border rounded-lg"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Amount</label>
            <input
              type="number"
              value={formData.requestedAmount}
              onChange={(e) => setFormData({ ...formData, requestedAmount: e.target.value })}
              className="w-full px-3 py-2 border rounded-lg"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Term (months)</label>
            <input
              type="number"
              value={formData.termMonths}
              onChange={(e) => setFormData({ ...formData, termMonths: e.target.value })}
              className="w-full px-3 py-2 border rounded-lg"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Interest Rate (%)</label>
            <input
              type="number"
              step="0.1"
              value={formData.interestRate}
              onChange={(e) => setFormData({ ...formData, interestRate: e.target.value })}
              className="w-full px-3 py-2 border rounded-lg"
              required
            />
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg flex items-center gap-2">
              <AlertCircle size={20} />
              <span className="text-sm">{error}</span>
            </div>
          )}

          <div className="flex gap-2">
            <button
              onClick={handleSubmit}
              disabled={loading}
              className="flex-1 bg-indigo-600 text-white py-2 rounded-lg hover:bg-indigo-700 disabled:opacity-50"
            >
              {loading ? 'Creating...' : 'Create'}
            </button>
            <button
              onClick={onClose}
              className="flex-1 bg-gray-200 text-gray-800 py-2 rounded-lg hover:bg-gray-300"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CreateApplicationModal;