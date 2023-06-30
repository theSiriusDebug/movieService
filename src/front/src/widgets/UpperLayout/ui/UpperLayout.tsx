import React, { memo, useCallback, useState } from "react";
import cls from "./UpperLayout.module.scss";
import { Col, Layout, Row, Button, Input, Space } from "antd";

interface UpperLayoutProps {
	className?: string;
}

export const UpperLayout = memo(({ className }: UpperLayoutProps) => {
	return (
		<Layout.Content>
			<Row justify="center">
				<Col span={14} style={{ backgroundColor: "gray", marginTop: 30, height: "400px" }}>
					<Row justify="start" align="middle">
						<Col span={16}>
							<Button className={cls.buttonUpperLayout}>by date</Button>
							<Button className={cls.buttonUpperLayout}>by alphabet</Button>
							<Button className={cls.buttonUpperLayout}>by raiting</Button>
						</Col>
						<Col span={8}>
							<Space.Compact>
								<Input />
								<Button className={cls.buttonUpperLayout}>Search</Button>
							</Space.Compact>
						</Col>
					</Row>
					<Row gutter={[16, 24]} style={{marginTop: "25px"}}>
						{new Array(20).fill(null).map((_, index) => (
							// eslint-disable-next-line react/no-array-index-key
							<Col className="gutter-row" span={6} key={index}>
								<div style={{textAlign: "center"}}>col-6</div>
							</Col>
						))}
					</Row>
				</Col>
			</Row>
		</Layout.Content>
	);
});
