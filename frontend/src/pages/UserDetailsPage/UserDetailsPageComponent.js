import React from 'react';
import {Button, Checkbox, Descriptions, Spin} from 'antd';
import moment from 'moment';
import AppLayout from '../../components/AppLayout';
import {useTranslation} from 'react-i18next';
import {useHistory} from "react-router-dom";
import 'antd/dist/antd.css';
import './UserDetailsPage.css'

const UserDetailsPage = (props) => {
    const {t} = useTranslation();
    const history = useHistory();
    const userDetails = props.userDetails;

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
            {userDetails ? (
                <div className="user-details-page-wrapper">
                    <h1 style={{fontWeight: "bold"}}>{t('text.userDetails')}</h1>
                    <div className="user-details">
                        <Descriptions className="table" column={1} bordered colon={false}>
                            <Descriptions.Item
                                label={`${t('userLabels.username')}:`}>{userDetails.username}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.firstName')}:`}>{userDetails.firstName}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.lastName')}:`}>{userDetails.lastName}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.email')}:`}>{userDetails.email}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.phoneNumber')}:`}>{userDetails.phoneNumber}</Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.activated')}:`}> {userDetails.activated ? t('text.yes') : t('text.no')} </Descriptions.Item>
                            <Descriptions.Item
                                label={`${t('userLabels.created')}:`}> {moment(userDetails.createdAt).format('DD-MM-YYYY, HH:MM')} </Descriptions.Item>
                            <Descriptions.Item label={`${t('userLabels.roles')}:`}>
                                {userDetails.accessLevelIds.map(accessLevelId => {
                                    let value;

                                    if (accessLevelId === 1)
                                        value = t('role.admin');
                                    else if (accessLevelId === 2)
                                        value = t('role.man');
                                    else if (accessLevelId === 3)
                                        value = t('role.client')

                                    return (
                                        <Checkbox key={value} indeterminate="true"> {value} </Checkbox>
                                    );
                                })}
                            </Descriptions.Item>
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

export default UserDetailsPage;