import React from 'react';
import { Link } from 'react-router-dom';
import './Catalog.css';

const Catalog = () => {
    return (
        <div className="home-container">
            {/* Логотип, который ведет на главную страницу */}
            <div className="logo-container">
                <Link to="/" className="logo">
                    TKO Fitness {/* Логотип — это текст */}
                </Link>
            </div>

            {/* Три кнопки, ведущие на другие страницы */}
            <div className="navigation-buttons">
                <button className="nav-button">Тренировки</button>
                <button className="nav-button">Программы</button>
                <button className="nav-button">Расчет ккал</button>
            </div>

            {/* Кнопки регистрации и входа */}
            <div className="auth-buttons">
                <button className="auth-button">Регистрация</button>
                <button className="auth-button">Вход</button>
            </div>


        </div>
    );
};

export default Catalog;