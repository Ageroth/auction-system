import React from 'react';
import {useHistory} from "react-router-dom";
import {Button, Descriptions, Spin} from 'antd';
import moment from 'moment';
import AppLayout from '../../components/AppLayout';
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css';
import './OwnDetailsPage.css'

const OwnDetailsPage = (props) => {
    const {t} = useTranslation();
    const history = useHistory();
    const myDetails = props.myDetails;

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
                <div className="own-details-page-wrapper">
                    <h1 style={{fontWeight: "bold"}}>{t('text.myDetails')}</h1>
                    <div className="own-details">
                        <Descriptions className="table" column={1} bordered colon={false}>
                            <Descriptions.Item
                                label={`${t('userLabels.username')}:`}>{myDetails.username}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.firstName')}:`}>{myDetails.firstName}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.lastName')}:`}>{myDetails.lastName}</Descriptions.Item>
                            <Descriptions.Item label={`${t('userLabels.email')}:`}>{myDetails.email}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.phoneNumber')}:`}>{myDetails.phoneNumber}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.activated')}:`}> {myDetails.activated ? t('text.yes') : t('text.no')} </Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.created')}:`}> {moment(myDetails.createdAt).format('DD-MM-YYYY HH:MM')} </Descriptions.Item>
                        </Descriptions>
                        <div className="buttons">
                            <Button type="primary" onClick={handleEditClick}> {t('text.edit')} </Button>
                            <Button type="primary"
                                    onClick={handlePasswordChangeClick}> {t('text.changePassword')} </Button>
                        </div>
                    </div>
                </div>
            ) : (
                <Spin size="large"/>
            )}
        </AppLayout>
    );
}

export default OwnDetailsPage;