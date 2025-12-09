import { Plus } from 'lucide-react';

const AffiliatesGrid = ({ affiliates, onRefresh, onCreateNew }) => {
  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-800">Affiliates</h2>
        <button
          onClick={onCreateNew}
          className="flex items-center gap-2 bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition"
        >
          <Plus size={20} />
          New Affiliate
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {affiliates.map((affiliate) => (
          <div key={affiliate.id} className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition">
            <div className="flex justify-between items-start mb-4">
              <div>
                <h3 className="font-semibold text-lg text-gray-800">
                  {affiliate.fullName}
                </h3>
                <p className="text-sm text-gray-600">{affiliate.document}</p>
              </div>
              <span
                className={`px-2 py-1 rounded text-xs font-semibold ${
                  affiliate.status === 'ACTIVE'
                    ? 'bg-green-100 text-green-800'
                    : 'bg-gray-100 text-gray-800'
                }`}
              >
                {affiliate.status}
              </span>
            </div>

            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-gray-600">Email:</span>
                <span className="text-gray-900 truncate ml-2">{affiliate.email}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Phone:</span>
                <span className="text-gray-900">{affiliate.phone || 'N/A'}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Salary:</span>
                <span className="text-gray-900 font-semibold">
                  ${affiliate.monthlySalary?.toLocaleString()}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Since:</span>
                <span className="text-gray-900">{affiliate.affiliationDate}</span>
              </div>
            </div>
          </div>
        ))}
      </div>

      {affiliates.length === 0 && (
        <div className="text-center py-12 text-gray-500 bg-white rounded-lg">
          No affiliates found
        </div>
      )}
    </div>
  );
};

export default AffiliatesGrid;