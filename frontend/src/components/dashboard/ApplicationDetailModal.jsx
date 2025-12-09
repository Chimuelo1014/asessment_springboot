const StatusBadge = ({ status }) => {
  const colors = {
    PENDING: 'bg-yellow-100 text-yellow-800',
    UNDER_REVIEW: 'bg-blue-100 text-blue-800',
    APPROVED: 'bg-green-100 text-green-800',
    REJECTED: 'bg-red-100 text-red-800',
  };
  return (
    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${colors[status]}`}>
      {status}
    </span>
  );
};

const RiskBadge = ({ level }) => {
  const colors = {
    LOW: 'bg-green-100 text-green-800',
    MEDIUM: 'bg-yellow-100 text-yellow-800',
    HIGH: 'bg-red-100 text-red-800',
  };
  return (
    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${colors[level]}`}>
      {level} RISK
    </span>
  );
};

const ApplicationDetailModal = ({ application, onClose }) => {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="p-6">
          <div className="flex justify-between items-start mb-6">
            <div>
              <h2 className="text-2xl font-bold text-gray-800">
                Application #{application.id}
              </h2>
              <p className="text-gray-600">{application.affiliateName}</p>
            </div>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 text-2xl"
            >
              ×
            </button>
          </div>

          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="text-sm text-gray-600">Status</label>
                <div className="mt-1">
                  <StatusBadge status={application.status} />
                </div>
              </div>
              <div>
                <label className="text-sm text-gray-600">Amount</label>
                <p className="text-lg font-semibold text-gray-900">
                  ${application.requestedAmount?.toLocaleString()}
                </p>
              </div>
              <div>
                <label className="text-sm text-gray-600">Term</label>
                <p className="text-lg font-semibold text-gray-900">
                  {application.termMonths} months
                </p>
              </div>
              <div>
                <label className="text-sm text-gray-600">Interest Rate</label>
                <p className="text-lg font-semibold text-gray-900">
                  {application.interestRate}%
                </p>
              </div>
            </div>

            {application.riskEvaluation && (
              <div className="bg-gray-50 rounded-lg p-4">
                <h3 className="font-semibold mb-3">Risk Evaluation</h3>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-gray-600">Score</label>
                    <p className="text-2xl font-bold text-gray-900">
                      {application.riskEvaluation.score}
                    </p>
                  </div>
                  <div>
                    <label className="text-sm text-gray-600">Risk Level</label>
                    <div className="mt-1">
                      <RiskBadge level={application.riskEvaluation.riskLevel} />
                    </div>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm text-gray-600">Recommendation</label>
                    <p className="text-gray-900 font-medium">
                      {application.riskEvaluation.recommendation}
                    </p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm text-gray-600">Message</label>
                    <p className="text-gray-900">
                      {application.riskEvaluation.message}
                    </p>
                  </div>
                </div>
              </div>
            )}

            {application.analystComments && (
              <div>
                <label className="text-sm text-gray-600">Analyst Comments</label>
                <p className="mt-1 text-gray-900 bg-gray-50 p-3 rounded">
                  {application.analystComments}
                </p>
              </div>
            )}

            <div className="grid grid-cols-2 gap-4 text-sm pt-4 border-t">
              <div>
                <label className="text-gray-600">Application Date</label>
                <p className="text-gray-900">{application.applicationDate}</p>
              </div>
              {application.evaluationDate && (
                <div>
                  <label className="text-gray-600">Evaluation Date</label>
                  <p className="text-gray-900">{application.evaluationDate}</p>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ApplicationDetailModal;