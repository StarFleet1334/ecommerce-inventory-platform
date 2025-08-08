import React, { useState, useEffect } from "react";
import { testApiConnection, checkApiHealth } from "../utils/apiTest";

const DebugInfo: React.FC = () => {
  const [apiHealth, setApiHealth] = useState<boolean | null>(null);
  const [apiTest, setApiTest] = useState<boolean | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const checkHealth = async () => {
      try {
        const health = await checkApiHealth();
        setApiHealth(health);
      } catch (error) {
        console.error("Health check failed:", error);
        setApiHealth(false);
      }
    };

    checkHealth();
  }, []);

  const runApiTest = async () => {
    setLoading(true);
    try {
      const result = await testApiConnection();
      setApiTest(result);
    } catch (error) {
      console.error("API test failed:", error);
      setApiTest(false);
    } finally {
      setLoading(false);
    }
  };

  if (import.meta.env.PROD && !import.meta.env.VITE_DEBUG) {
    return null; // Don't show debug info in production unless debug is enabled
  }

  return (
    <div className="fixed bottom-4 left-4 bg-white border border-gray-200 rounded-lg p-4 shadow-lg max-w-sm">
      <h4 className="font-semibold text-gray-900 mb-2">Debug Info</h4>
      <div className="space-y-2 text-sm">
        <div>
          <span className="font-medium">Environment:</span>
          <span className="ml-2 text-gray-600">
            {import.meta.env.PROD ? "Production" : "Development"}
          </span>
        </div>
        <div>
          <span className="font-medium">API Base URL:</span>
          <span className="ml-2 text-gray-600">
            {import.meta.env.VITE_API_BASE_URL || "/api/inventory"}
          </span>
        </div>
        <div>
          <span className="font-medium">API Health:</span>
          <span
            className={`ml-2 ${
              apiHealth === true
                ? "text-green-600"
                : apiHealth === false
                  ? "text-red-600"
                  : "text-gray-600"
            }`}
          >
            {apiHealth === null
              ? "Checking..."
              : apiHealth
                ? "✅ Healthy"
                : "❌ Unhealthy"}
          </span>
        </div>
        <div>
          <span className="font-medium">API Test:</span>
          <span
            className={`ml-2 ${
              apiTest === true
                ? "text-green-600"
                : apiTest === false
                  ? "text-red-600"
                  : "text-gray-600"
            }`}
          >
            {apiTest === null
              ? "Not tested"
              : apiTest
                ? "✅ Passed"
                : "❌ Failed"}
          </span>
        </div>
        <button
          onClick={runApiTest}
          disabled={loading}
          className="mt-2 px-3 py-1 bg-blue-600 text-white text-xs rounded hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? "Testing..." : "Test API Connection"}
        </button>
      </div>
    </div>
  );
};

export default DebugInfo;
