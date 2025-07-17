import React from 'react';

interface EntityListProps {
  items: Record<string, any>[];
  onDelete: (id: string) => Promise<void>;
  loading: boolean;
  title: string;
  idField: string;
  displayFields: Array<{
    key: string;
    label: string;
    type?: 'text' | 'number' | 'currency' | 'date';
  }>;
  emptyMessage: string;
}

const EntityList: React.FC<EntityListProps> = ({
  items,
  onDelete,
  loading,
  title,
  idField,
  displayFields,
  emptyMessage
}) => {
  const handleDelete = async (id: string) => {
    if (window.confirm('Are you sure you want to delete this item?')) {
      await onDelete(id);
    }
  };

  const formatValue = (value: any, type?: string) => {
    if (value === null || value === undefined) return '-';
    
    switch (type) {
      case 'currency':
        return typeof value === 'number' ? `$${value.toFixed(2)}` : value;
      case 'number':
        return typeof value === 'number' ? value.toLocaleString() : value;
      case 'date':
        return new Date(value).toLocaleDateString();
      default:
        return value;
    }
  };

  if (items.length === 0) {
    return (
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
        <h3 className="text-lg font-medium text-gray-900 mb-4">{title}</h3>
        <div className="text-center py-8">
          <p className="text-gray-500">{emptyMessage}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
      <h3 className="text-lg font-medium text-gray-900 mb-4">{title}</h3>
      <div className="space-y-4">
        {items.map((item) => (
          <div
            key={item[idField]}
            className="bg-gray-50 border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
          >
            <div className="flex justify-between items-start">
              <div className="flex-1">
                {displayFields.map((field) => (
                  <div key={field.key} className="mb-2">
                    <span className="text-sm font-medium text-gray-700">
                      {field.label}:
                    </span>
                    <span className="ml-2 text-gray-900">
                      {formatValue(item[field.key], field.type)}
                    </span>
                  </div>
                ))}
              </div>
              <button
                onClick={() => handleDelete(item[idField])}
                disabled={loading}
                className="ml-4 px-3 py-1 text-sm font-medium text-red-600 hover:text-red-800 hover:bg-red-50 rounded-md disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EntityList; 