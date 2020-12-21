import React from 'react';
import {Link} from 'react-router-dom';
import AppLayout from '../../../components/AppLayout'
import {useTranslation} from 'react-i18next';
import image from '../../../assets/404.svg';
import './NotFoundPage.css'

const NotFoundPage = () => {
    const {t} = useTranslation();

    return (
        <AppLayout>
            <div className="not-found-page-wrapper">
                <img className="image" src={image} alt="Error 404"/>
                <div className="text">
                    <h1>  {t('text.404')} </h1>
                    <Link to="/">
                        {t('text.home')}
                    </Link>
                </div>
            </div>
        </AppLayout>
    );
}

export default NotFoundPage;