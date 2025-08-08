import React, { useState } from "react";

interface FormField {
  name: string;
  label: string;
  type: "text" | "number" | "email" | "textarea";
  required?: boolean;
  placeholder?: string;
}

interface EntityFormProps {
  fields: FormField[];
  onSubmit: (data: Record<string, any>) => Promise<void>;
  loading: boolean;
  title: string;
  submitLabel: string;
  initialData?: Record<string, any>;
}

const EntityForm: React.FC<EntityFormProps> = ({
  fields,
  onSubmit,
  loading,
  title,
  submitLabel,
  initialData = {},
}) => {
  const [formData, setFormData] = useState<Record<string, any>>(initialData);

  const handleChange = (name: string, value: any) => {
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await onSubmit(formData);
    // Reset form after successful submission
    setFormData({});
  };

  const renderField = (field: FormField) => {
    const commonProps = {
      id: field.name,
      name: field.name,
      required: field.required,
      placeholder: field.placeholder,
      value: formData[field.name] || "",
      onChange: (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
      ) => {
        const value =
          field.type === "number"
            ? parseFloat(e.target.value) || 0
            : e.target.value;
        handleChange(field.name, value);
      },
      className:
        "mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500",
    };

    switch (field.type) {
      case "textarea":
        return <textarea {...commonProps} rows={3} />;
      case "number":
        return <input {...commonProps} type="number" step="0.01" min="0" />;
      case "email":
        return <input {...commonProps} type="email" />;
      default:
        return <input {...commonProps} type="text" />;
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
      <h2 className="text-xl font-semibold text-gray-900 mb-6">{title}</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        {fields.map((field) => (
          <div key={field.name}>
            <label
              htmlFor={field.name}
              className="block text-sm font-medium text-gray-700"
            >
              {field.label}
            </label>
            {renderField(field)}
          </div>
        ))}
        <button
          type="submit"
          disabled={loading}
          className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {loading ? "Processing..." : submitLabel}
        </button>
      </form>
    </div>
  );
};

export default EntityForm;
