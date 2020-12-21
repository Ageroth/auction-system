import React from 'react';
import {Link} from 'react-router-dom'
import withBreadcrumbs from 'react-router-breadcrumbs-hoc';
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css';
import './AppBreadcrumbs.css'

const routes = [
    {path: '/', breadcrumb: 'pageName.home'},
    {path: '/login', breadcrumb: null},
    {path: '/signup', breadcrumb: null},
    {path: '/users', breadcrumb: 'pageName.users'},
    {path: '/users/add', breadcrumb: 'pageName.userAdd'},
    {path: '/users/:id/edit', breadcrumb: 'pageName.edit'},
    {path: '/users/:id/password_change', breadcrumb: 'pageName.passwordChange'},
    {path: '/users/:id', breadcrumb: 'pageName.userDetails'},
    {path: '/my_profile/edit', breadcrumb: 'pageName.edit'},
    {path: '/my_profile/password_change', breadcrumb: 'pageName.passwordChange'},
    {path: '/my_profile', breadcrumb: 'pageName.myProfile'},
    {path: '/password_reset', breadcrumb: null},
    {path: '/password_reset/:resetPasswordCode', breadcrumb: 'pageName.passwordReset'},
    {path: '/activation', breadcrumb: null},
    {path: '/activation/:activationCode', breadcrumb: null},
    {path: '/auctions', breadcrumb: 'pageName.auctions'},
    {path: '/auctions/:auctionId/edit', breadcrumb: 'pageName.edit'},
    {path: '/auctions/:auctionId', breadcrumb: 'pageName.auctionDetails'},
    {path: '/my_auctions', breadcrumb: 'pageName.myAuctions'},
    {path: '/my_auctions/add', breadcrumb: 'pageName.auctionAdd'},
    {path: '/my_auctions/:auctionId/edit', breadcrumb: 'pageName.edit'},
    {path: '/my_auctions/:auctionId', breadcrumb: 'pageName.auctionDetails'},
    {path: '/my_biddings', breadcrumb: 'pageName.myBiddings'}
];

const AppBreadcrumbs = ({breadcrumbs}) => {
    const {t} = useTranslation();

    return (
        <>
            {breadcrumbs.length > 1 ?
                (
                    <>
                        <div className="breadcrumbs-wrapper">
                            {breadcrumbs.map(({breadcrumb, match}, index) => (
                                <div className="bc" key={match.url}>
                                    <Link to={match.url || ""}>{' '} {t(breadcrumb.props.children)}</Link>
                                    {index < breadcrumbs.length - 1 ? " >" : null}
                                    &nbsp;
                                </div>
                            ))}
                        </div>
                    </>
                ) : null
            }
        </>
    );
};

export default withBreadcrumbs(routes)(AppBreadcrumbs);