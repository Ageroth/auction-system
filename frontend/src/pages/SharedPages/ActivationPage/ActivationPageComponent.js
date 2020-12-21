import React from 'react';
import {useTranslation} from 'react-i18next';
import AppLayout from '../../../components/AppLayout'
import './ActivationPage.css'

const ActivationPage = (props) => {
    const {t} = useTranslation();
    const error = props.error;

    return (
        <AppLayout>
            {error ? (
                <div className="activation-box">
                    <h3> {t('message.title.activationFailed')} </h3>
                    <p> {t('message.content.activationFailed')} </p>
                </div>
            ) : (
                <div className="activation-box">
                    <h3> {t('message.title.activationSucceded')} </h3>
                    <p> {t('message.content.activationSucceded')} </p>
                </div>
            )}
        </AppLayout>
    );
}

export default ActivationPage;