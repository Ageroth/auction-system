import React from 'react';
import { Descriptions, Button, Spin } from 'antd';
import AppLayout from '../../components/AppLayout';
import { useTranslation } from 'react-i18next';
import { useHistory } from "react-router-dom";
import 'antd/dist/antd.css';
import './OwnDetailsPage.css'

const OwnDetailsPage = (props) => {
    const {t} = useTranslation();
    const history = useHistory();
    const myDetails = props.myDetails;

    const formatDate = (date) => {
        return new Date(date).toLocaleString([]);
    }

    const handleEditClick = () => {
        const currentLocation = history.location.pathname;

        history.push(`${currentLocation}/edit`);
    }

    const handlePasswordChangeClick = () => {
        const currentLocation = history.location.pathname;

        history.push(`${currentLocation}/password_change`);
    }

    return (
        <AppLayout>
            {myDetails ? (
                <div className="my-details-page-wrapper">
                    <Descriptions className="table" title={t('text.yourDetails')} column={1} bordered>
                        <Descriptions.Item label={t('userLabels.username')}> {myDetails.username} </Descriptions.Item>
                        <Descriptions.Item label={t('userLabels.firstName')}> {myDetails.firstName} </Descriptions.Item>
                        <Descriptions.Item label={t('userLabels.lastName')}> {myDetails.lastName} </Descriptions.Item>
                        <Descriptions.Item label={t('userLabels.email')}> {myDetails.email} </Descriptions.Item>
                        <Descriptions.Item label={t('userLabels.phoneNumber')}> {myDetails.phoneNumber} </Descriptions.Item>
                        <Descriptions.Item label={t('userLabels.activated')}> {myDetails.activated ? t('text.yes') : t('text.no')} </Descriptions.Item>
                        <Descriptions.Item label={t('userLabels.created')}> {formatDate(myDetails.createdAt)} </Descriptions.Item>
                    </Descriptions>
                    <div className="buttons">
                        <Button type="primary" onClick={handleEditClick}> {t('text.edit')} </Button>
                        <Button type="primary" onClick={handlePasswordChangeClick}> {t('text.changePassword')} </Button>
                    </div>
                </div>
            ) : (
                    <Spin size="large" />
            )}
        </AppLayout>
    );
}

export default OwnDetailsPage;