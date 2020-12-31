import React from 'react';
import {Link} from 'react-router-dom';
import AppLayout from '../../../components/AppLayout'
import {useTranslation} from 'react-i18next';
import {Button, Result} from 'antd';

const InternalServerErrorPage = () => {
    const {t} = useTranslation();

    return (
        <AppLayout>
            <div className="internal-server-error-page-wrapper">
                <Result
                    status="500"
                    title="500"
                    subTitle={t('text.500')}
                    extra={<Link to={"/"}><Button type="primary">{t('text.home')}</Button></Link>}
                />
            </div>
        </AppLayout>
    );
}

export default InternalServerErrorPage;