import React from 'react';
import { Descriptions, Checkbox, Button } from 'antd';
import AppLayout from '../../components/AppLayout';
import { useTranslation } from 'react-i18next';
import 'antd/dist/antd.css';
import './UserDetailsPage.css'


const UserDetailsPage = (props) => {
    const {t} = useTranslation();
    const userDetails = props.userDetails;

    const formatDate = (string) => {
        return new Date(string).toLocaleString([]);
    }

    return (
        <AppLayout>
            <div className="user-details-page-wrapper">
                <Descriptions className="table" title={t('text.userDetails')} column={1} bordered>
                <Descriptions.Item label={t('userLabels.username')}> {userDetails.username} </Descriptions.Item>
                <Descriptions.Item label={t('userLabels.firstName')}> {userDetails.firstName} </Descriptions.Item>
                <Descriptions.Item label={t('userLabels.lastName')}> {userDetails.lastName} </Descriptions.Item>
                <Descriptions.Item label={t('userLabels.email')}> {userDetails.email} </Descriptions.Item>
                <Descriptions.Item label={t('userLabels.phoneNumber')}> {userDetails.phoneNumber} </Descriptions.Item>
                <Descriptions.Item label={t('userLabels.activated')}> {userDetails.activated ? t('text.yes') : t('text.no')} </Descriptions.Item>
                <Descriptions.Item label={t('userLabels.created')}> {formatDate(userDetails.createdAt)} </Descriptions.Item>
                <Descriptions.Item label={t('userLabels.roles')}>
                    {userDetails.userAccessLevelsName.map(userAccessLevelName => {
                        let value;

                        if (userAccessLevelName === "ADMINISTRATOR") 
                            value = t('role.admin');
                        
                        else if (userAccessLevelName === "MODERATOR")
                            value = t('role.mod');
                        else 
                            value =  t('role.client')

                        return (
                            <Checkbox key={value} indeterminate="true"> {value} </Checkbox>
                        );
                    })} 
                </Descriptions.Item>
            </Descriptions>
            <div className="buttons">
                <Button type="primary">Edit</Button>
                <Button type="primary" >Change password</Button>
                <Button type="primary">Modify roles</Button>
            </div>
        </div>
      </AppLayout>
    );
}

export default UserDetailsPage;