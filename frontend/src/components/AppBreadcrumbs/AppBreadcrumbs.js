import React from 'react';
import {Link, useHistory} from 'react-router-dom';
import {Breadcrumb} from 'antd';
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css';
import './AppBreadcrumbs.css'

const AppBreadcrumbs = () => {
    const {t} = useTranslation();
    const history = useHistory();
    const breadcrumbNameMap = {
        '/users': t('pageName.users'),
        '/users/add': t('pageName.userAdd'),
        '/users/details': t('pageName.userDetails'),
        '/users/details/edit': t('pageName.edit'),
        '/users/details/password_change': t('pageName.passwordChange'),
        '/my_profile': t('pageName.myProfile'),
        '/my_profile/edit': t('pageName.edit'),
        '/my_profile/password_change': t('pageName.passwordChange'),
        '/login': t('pageName.login'),
        '/signup': t('pageName.signup'),
        '/password_reset': t('pageName.passwordReset'),
        '/activation': t('pageName.activation'),
        '/auctions': t('pageName.auctions'),
    };
    const pathSnippets = history.location.pathname.split('/').filter(i => i);
    const nameSnippets = pathSnippets.map(path => isNaN(path) ? path : "details");
    const extraBreadcrumbItems = pathSnippets.map((_, index) => {
        const url = `/${pathSnippets.slice(0, index + 1).join('/')}`;
        const name = `/${nameSnippets.slice(0, index + 1).join('/')}`;

        return (
            <Breadcrumb.Item key={url}>
                <Link to={url}>{breadcrumbNameMap[name]}</Link>
            </Breadcrumb.Item>
        );
    });

    let breadcrumbItems;

    extraBreadcrumbItems.length > 0 ? (
        breadcrumbItems = [
            <Breadcrumb.Item key="home">
                <Link to="/"> {t('pageName.home')} </Link>
            </Breadcrumb.Item>
        ].concat(extraBreadcrumbItems)
    ) : (
        breadcrumbItems = null
    )

    return (
        <Breadcrumb className="app-breadcrumbs" separator=">">{breadcrumbItems}</Breadcrumb>
    );
}

export default AppBreadcrumbs;