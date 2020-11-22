import React from 'react';
import { Link } from 'react-router-dom';
import { Layout, Breadcrumb } from 'antd';
import { useHistory } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import AppHeader from '../AppHeader'
import 'antd/dist/antd.css';
import './AppLayout.css'


const { Footer } = Layout;

const AppLayout = ({children}) => {
    const {t} = useTranslation();
    const history = useHistory();
    const breadcrumbNameMap = {
        '/users': t('pathName.users'),
        '/users/details': t('pathName.userDetails'),
        '/users/details/edit': t('pathName.edit'),
        '/users/details/password_change': t('pathName.passwordChange'),
        '/my_profile': t('pathName.myProfile'),
        '/my_profile/edit': t('pathName.edit'),
        '/my_profile/password_change': t('pathName.passwordChange')
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
    
    extraBreadcrumbItems.length > 1 ? (
        breadcrumbItems = [
            <Breadcrumb.Item key="home">
                <Link to="/"> {t('pathName.home')} </Link>
            </Breadcrumb.Item>
        ].concat(extraBreadcrumbItems)
    ) : ( 
        breadcrumbItems = null
    )

    return (
        <Layout className="app-layout">
            <AppHeader/>
            <Breadcrumb className="app-breadcrumbs" separator=">">{breadcrumbItems}</Breadcrumb>
            <div className="app-content">
                {children}
            </div>
            <Footer className="app-footer"> Auction System ©2020 </Footer>
        </Layout>
    );
}

export default AppLayout;