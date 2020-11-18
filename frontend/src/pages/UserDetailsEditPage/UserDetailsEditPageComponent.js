import React from 'react';
import { Button, Form, Input, Checkbox, Spin} from 'antd'
import AppLayout from '../../components/AppLayout';
import { useTranslation } from 'react-i18next';
import 'antd/dist/antd.css';
import './UserDetailsEditPage.css'


const UserDetailsEditPage = (props) => {
    const [form] = Form.useForm();
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;
    const userDetails = props.userDetails;

    const onFinish = (values) => {
        const payload = Object.assign({}, values);
        props.onSubmit(payload);
    }
    
    return (
        <AppLayout>
            {userDetails ? (
                <Form
                    form={form}
                    layout="vertical"
                    name="user_edit_form"
                    className="user-edit-form"
                    onFinish={onFinish}
                    scrollToFirstError
                    initialValues={{
                        'userAccessLevelNames': userDetails.userAccessLevelNames
                    }}
                >
                    <Form.Item
                        label={t('userLabels.firstName')}
                        name="firstName"
                        initialValue={userDetails.firstName}
                        rules={[
                            {
                                required: true,
                                message: t('validation.required')
                            },
                            {
                                pattern: new RegExp("^[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$"),
                                message: t('validation.regex.name')
                            },
                            {
                                max: 32,
                                message: t('validation.max32chars')
                            }
                        ]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label={t('userLabels.lastName')}
                        name="lastName"
                        initialValue={userDetails.lastName}
                        rules={[
                            {
                                required: true,
                                message: t('validation.required')
                            },
                            {
                                pattern: new RegExp("^[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$"),
                                message: t('validation.regex.name')
                            },
                            {
                                max: 32,
                                message: t('validation.max32chars')
                            }
                        ]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label={t('userLabels.phoneNumber')}
                        name="phoneNumber"
                        initialValue={userDetails.phoneNumber}
                        rules={[
                            {
                                required: true,
                                message: t('validation.required')
                            },
                            {
                                pattern: new RegExp("^[0-9]{9,10}$"),
                                message: t('validation.regex.phoneNumber')
                            }
                        ]} 
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label={t('userLabels.roles')}
                        name="userAccessLevelNames"
                        rules={[
                            {
                                required: true,
                                message: t('validation.required')
                            }
                        ]} 
                    >
                        <Checkbox.Group>
                            <Checkbox
                                value="ADMINISTRATOR"
                            >
                                {t('role.admin')}       
                            </Checkbox>
                    
                            <Checkbox
                                value="MODERATOR" 
                            >
                                {t('role.mod')}  
                            </Checkbox>
         
                            <Checkbox
                                value="CLIENT"
                            >
                                {t('role.client')}  
                            </Checkbox>
                        </Checkbox.Group>
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" className="signup-form-button" disabled={isSubmitting}>
                            Edit
                        </Button>
                    </Form.Item>
                </Form>
            ) : (
                <Spin size="large" />
            )}
      </AppLayout>
    );
}

export default UserDetailsEditPage;