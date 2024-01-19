import React, { Component } from 'react';
import { View, StyleSheet, TouchableWithoutFeedback } from 'react-native';
import * as C from './constants';
import Grid from './Grid';

class BarChart extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  _handlePress = (e, dataPoint, index) => {
    if (this.props.onDataPointPress) {
      this.props.onDataPointPress(e, dataPoint, index);
    }
  };

  _drawBar = (dato, index) => {
    const backgroundColor = this.props.color[0] || C.BLUE;
    const HEIGHT = this.props.height;
    const WIDTH = this.props.width;

    const width = WIDTH / this.props.data.length * 0.8;
    const divisor = Math.max(...this.props.data.map((item) => item.conteo));
    const scale = HEIGHT / divisor;
    let height = HEIGHT - (dato.conteo * scale);

    height = Math.max(20, height);

    return (
      <TouchableWithoutFeedback
        key={index}
        onPress={(e) => this._handlePress(e, dato.conteo, index)}
      >
        <View
          style={{
            borderTopLeftRadius: this.props.cornerRadius || 0,
            borderTopRightRadius: this.props.cornerRadius || 0,
            backgroundColor,
            width,
            height,
          }}
        />
      </TouchableWithoutFeedback>
    );
  };

  render() {
    return (
      <View ref="container" style={[styles.default]}>
        <Grid {...this.props} />
        {this.props.data.map((dato, index) => this._drawBar(dato, index))}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  default: {
    flex: 1,
    alignItems: 'flex-end',
    flexDirection: 'row',
    justifyContent: 'space-around',
  },
});

export default BarChart;
