import React, { Suspense, FC } from 'react';
import { classNames } from 'shared/lib/classNames/classNames';
import { useTheme } from 'app/providers/ThemeProvider';
import { AppRouter } from 'app/providers/router';
import { Navbar } from 'widgets/Navbar';
import { UpperLayout } from 'widgets/UpperLayout';
import { Col, Layout, Row, Button, Input, Space } from 'antd';
const App: FC = () => {
  const { theme } = useTheme();

  return (
    <div className={classNames('app', {}, [theme])}>
      <Suspense fallback="">
        <Layout.Content>
          <Row justify="center">
            <Col span={16}>
              <Navbar />
              <div className="content-page">
                <AppRouter />
              </div>
            </Col>
          </Row>
        </Layout.Content>
      </Suspense>
    </div>
  );
};

export default App;
