import React from 'react';
import AppLayout from '../../../components/AppLayout'
import {useTranslation} from 'react-i18next';
import {Button, Result} from 'antd';
import {Link} from "react-router-dom";

const ForbiddenPage = () => {
    const {t} = useTranslation();

    return (
        <AppLayout>
            <div className="forbidden-page-wrapper">
                <Result
                    status="403"
                    title="403"
                    subTitle={t('text.403')}
                    extra={<Link to={"/"}><Button type="primary">{t('text.home')}</Button></Link>}
                />
            </div>
        </AppLayout>
    );
}

export default ForbiddenPage;