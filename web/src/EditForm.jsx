import React, { useState } from 'react';
import './EditForm.css';

const EditForm = ({ profileData, onClose, onSave }) => {
    const [formData, setFormData] = useState({
        name: profileData.name || '',
        login: profileData.login || '',
        height: profileData.height || '',
        weight: profileData.weight || '',
        birthDay: profileData.birthDay || '',
        targetKcal: profileData.targetKcal || '',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = () => {
        onSave(formData);
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>Редактировать профиль</h2>
                <label>Имя: <input type="text" name="name" value={formData.name} onChange={handleChange} /></label>
                <label>Логин: <input type="text" name="login" value={formData.login} onChange={handleChange} /></label>
                <label>Рост (см): <input type="number" name="height" value={formData.height} onChange={handleChange} /></label>
                <label>Вес (кг): <input type="number" name="weight" value={formData.weight} onChange={handleChange} /></label>
                <label>Дата рождения: <input type="date" name="birthDay" value={formData.birthDay} onChange={handleChange} /></label>
                <label>Целевые ккал: <input type="number" name="targetKcal" value={formData.targetKcal} onChange={handleChange} /></label>

                <div className="modal-buttons">
                    <button onClick={handleSubmit} className="auth-button">Сохранить</button>
                    <button onClick={onClose} className="auth-button">Отмена</button>
                </div>
            </div>
        </div>
    );
};

export default EditForm;
