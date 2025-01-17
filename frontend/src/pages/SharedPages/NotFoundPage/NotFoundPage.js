import React from 'react';
import {Link} from 'react-router-dom';
import AppLayout from '../../../components/AppLayout'
import {useTranslation} from 'react-i18next';
import {Button, Result} from 'antd';

const NotFoundPage = () => {
    const {t} = useTranslation();

    return (
        <AppLayout>
            <div className="not-found-page-wrapper">
                <Result
                    status="404"
                    title="404"
                    subTitle={t('text.404')}
                    extra={<Link to={"/"}><Button type="primary">{t('text.home')}</Button></Link>}
                />
            </div>
        </AppLayout>
    );
}

export default NotFoundPage;