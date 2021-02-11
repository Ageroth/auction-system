import React from 'react';
import {Layout} from 'antd';
import AppHeader from '../AppHeader'
import AppBreadcrumbs from '../AppBreadcrumbs'
import 'antd/dist/antd.css';
import './AppLayout.css'

const {Footer} = Layout;

const AppLayout = ({children}) => {
    return (
        <Layout className="app-layout">
            <AppHeader/>
            <div className="app-breadcrumbs">
                <AppBreadcrumbs/>
            </div>
            <div className="app-content">
                {children}
            </div>
            <Footer className="app-footer"> Auction System ©2021 </Footer>
        </Layout>
    );
}

export default AppLayout;