package com.stereokrauts.stereoscope.mixer.roland.m380;

import static com.stereokrauts.lib.binary.ByteStringUtil.normalize;

import com.stereokrauts.stereoscope.helper.tests.MessagingSysexAssociation;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;

public class TestData {
	public MessagingSysexAssociation[] testData = {

			/* INPUT */
			// input fader
			new MessagingSysexAssociation("input1-level-min", normalize("F0	41	00	00	00	24	12	03	00	00	0E	00 64	0B	F7"), new MsgChannelLevelChanged(0, 1)),  //   +100
			new MessagingSysexAssociation("input1-level-max", normalize("F0	41	00	00	00	24	12	03	00	00	0E	40 00	2F	F7"), new MsgChannelLevelChanged(0, 0)),  //  -8192 (-Inf)

			// mute
			new MessagingSysexAssociation("input1-mute-on", normalize("F0	41	00	00	00	24	12	03	00	00	0C	01	70	F7"), new MsgChannelOnChanged(0, true)),  //  on
			new MessagingSysexAssociation("input1-mute-off", normalize("F0	41	00	00	00	24	12	03	00	00	0C	00	71	F7"), new MsgChannelOnChanged(0, false)),  // off

			// channel solo and solo position (monitor), both sent on a solo event
			//new MessagingSysexAssociation("input1-solo-position", normalize("F0	41	00	00	00	24	12	07	00	00	02	00	77	F7"), new MsgChannelLevelChanged(0, 0)),  // input pfl
			//new MessagingSysexAssociation("input1-channel-solo-on", normalize("F0	41	00	00	00	24	12	03	00	00	0D	01	6F	F7"), new MsgChannelLevelChanged(0, 0)),  //  on
			//new MessagingSysexAssociation("input1-solo-position", normalize("F0	41	00	00	00	24	12	07	00	00	02	04	73	F7"), new MsgChannelLevelChanged(0, 0)),  //  monitor source
			//new MessagingSysexAssociation("input1-channel-solo-off", normalize("F0	41	00	00	00	24	12	03	00	00	0D	00	70	F7"), new MsgChannelLevelChanged(0, 0)),  //  off

			// panning
			new MessagingSysexAssociation("input1-pan-l", normalize("F0	41	00	00	00	24	12	03	00	00	10	01	6C	F7"), new MsgInputPan(0, -1.0f)),     // 63L (001)
			new MessagingSysexAssociation("input1-pan-r", normalize("F0	41	00	00	00	24	12	03	00	00	10	7F	6E	F7"), new MsgInputPan(0, 1.0f)),      // 63R (127)
			new MessagingSysexAssociation("input1-pan-c", normalize("F0	41	00	00	00	24	12	03	00	00	10	40	2D	F7"), new MsgInputPan(0, 0.0f)),  //   C  (64)


			/* PARAMETRIC EQUALIZER */
			// frequency 
			new MessagingSysexAssociation("input1-freq-b1-01", normalize("F0	41	00	00	00	24	12	03	00	00	55	00 00 14 14	F7"), new MsgInputPeqF(0, 0, 0.0f)),  // b1, 20Hz
			new MessagingSysexAssociation("input1-freq-b1-02", normalize("F0	41	00	00	00	24	12	03	00	00	55	00 07 68 39	F7"), new MsgInputPeqF(0, 0, 1.0f)),  // b1,  1kHz
			new MessagingSysexAssociation("input1-freq-b2-01", normalize("F0	41	00	00	00	24	12	03	00	00	5A	00 00 14 0F	F7"), new MsgInputPeqF(0, 1, 0.0f)),  // b2, 20Hz
			new MessagingSysexAssociation("input1-freq-b2-02", normalize("F0	41	00	00	00	24	12	03	00	00	5A	01 1C 20 66	F7"), new MsgInputPeqF(0, 1, 1.0f)),  // b2, 20kHz
			new MessagingSysexAssociation("input1-freq-b3-01", normalize("F0	41	00	00	00	24	12	03	00	00	61	00 00 14 08	F7"), new MsgInputPeqF(0, 2, 0.0f)),  // b3, 20Hz
			new MessagingSysexAssociation("input1-freq-b3-02", normalize("F0	41	00	00	00	24	12	03	00	00	61	01 1C 20 5F	F7"), new MsgInputPeqF(0, 2, 1.0f)),  // b3, 20kHz
			new MessagingSysexAssociation("input1-freq-b4-01", normalize("F0	41	00	00	00	24	12	03	00	00	68	00 07 68 26	F7"), new MsgInputPeqF(0, 3, 0.0f)),  // b4,  1kHz
			new MessagingSysexAssociation("input1-freq-b4-02", normalize("F0	41	00	00	00	24	12	03	00	00	68	01 1C 20 58	F7"), new MsgInputPeqF(0, 3, 1.0f)),  // b4, 20kHz

			// gain 
			new MessagingSysexAssociation("input1-gain-min", normalize("F0	41	00	00	00	24	12	03	00	00	53	7E 6A 42	F7"), new MsgInputPeqG(0, 0, -1.0f)),  // c1, b1, -150
			new MessagingSysexAssociation("input1-gain-mid", normalize("F0	41	00	00	00	24	12	03	00	00	53	00 00 2A	F7"), new MsgInputPeqG(0, 0,  0.0f)),  // c1, b1,    0
			new MessagingSysexAssociation("input1-gain-max", normalize("F0	41	00	00	00	24	12	03	00	00	53	01 16 13	F7"), new MsgInputPeqG(0, 0,  1.0f)),  // c1, b1, +150

			new MessagingSysexAssociation("input2-gain-min", normalize("F0	41	00	00	00	24	12	03	01	00	58	7E 6A 3C	F7"), new MsgInputPeqG(1, 1, -1.0f)),  // c2, b2, -150
			new MessagingSysexAssociation("input2-gain-mid", normalize("F0	41	00	00	00	24	12	03	01	00	58	00 00 24	F7"), new MsgInputPeqG(1, 1,  0.0f)),  // c2, b2,    0
			new MessagingSysexAssociation("input2-gain-max", normalize("F0	41	00	00	00	24	12	03	01	00	58	01 16 0D	F7"), new MsgInputPeqG(1, 1,  1.0f)),  // c2, b2, +150

			new MessagingSysexAssociation("input3-gain-min", normalize("F0	41	00	00	00	24	12	03	02	00	5F	7E 6A 34	F7"), new MsgInputPeqG(2, 2, -1.0f)),  // c3, b3, -150
			new MessagingSysexAssociation("input3-gain-mid", normalize("F0	41	00	00	00	24	12	03	02	00	5F	00 00 1C	F7"), new MsgInputPeqG(2, 2,  0.0f)),  // c3, b3,    0
			new MessagingSysexAssociation("input3-gain-max", normalize("F0	41	00	00	00	24	12	03	02	00	5F	01 16 05	F7"), new MsgInputPeqG(2, 2,  1.0f)),  // c3, b3, +150

			new MessagingSysexAssociation("input4-gain-min", normalize("F0	41	00	00	00	24	12	03	03	00	66	7E 6A 2C	F7"), new MsgInputPeqG(3, 3, -1.0f)),  // c4, b4, -150
			new MessagingSysexAssociation("input4-gain-mid", normalize("F0	41	00	00	00	24	12	03	03	00	66	00 00 14	F7"), new MsgInputPeqG(3, 3,  0.0f)),  // c4, b4,    0
			new MessagingSysexAssociation("input4-gain-max", normalize("F0	41	00	00	00	24	12	03	03	00	66	01 16 7D	F7"), new MsgInputPeqG(3, 3,  1.0f)),  // c4, b4, +150

			// quality
			new MessagingSysexAssociation("input-q-lomid-min", normalize("F0	41	00	00	00	24	12	03	00	00	5D	00 24 7C	F7"), new MsgInputPeqQ(0, 1,  0.0f)),  // c1, b2,   36
			new MessagingSysexAssociation("input-q-himid-max", normalize("F0	41	00	00	00	24	12	03	00	00	5D	0C 40 54	F7"), new MsgInputPeqQ(0, 1,  1.0f)),  // c1, b2, 1600

			new MessagingSysexAssociation("input-q-himid-min", normalize("F0	41	00	00	00	24	12	03	00	00	64	00 24 75	F7"), new MsgInputPeqQ(0, 2,  0.0f)),  // c1, b3,   36
			new MessagingSysexAssociation("input-q-himid-max", normalize("F0	41	00	00	00	24	12	03	00	00	64	0C 40 4D	F7"), new MsgInputPeqQ(0, 2,  1.0f)),  // c1, b3, 1600


			/* GATE */ 
			// threshold
			new MessagingSysexAssociation("dyn1-thr-01", normalize("F0	41	00	00	00	24	12	03	00	00	33	79 60 71	F7"), new MsgInputDynaThreshold(0, 0,  0.0f)),  // -800
			new MessagingSysexAssociation("dyn1-thr-02", normalize("F0	41	00	00	00	24	12	03	00	00	33	00 00 4A	F7"), new MsgInputDynaThreshold(0, 0,  1.0f)),  //    0

			// range 
			new MessagingSysexAssociation("dyn1-rng-01", normalize("F0	41	00	00	00	24	12	03	00	00	37	40 00 06	F7"), new MsgInputDynaRange(0, 0, 0.0f)),  //  -Inf
			new MessagingSysexAssociation("dyn1-rng-02", normalize("F0	41	00	00	00	24	12	03	00	00	37	00 00 46	F7"), new MsgInputDynaRange(0, 0, 1.0f)),  //     0

			// attack 
			new MessagingSysexAssociation("dyn1-att-01", normalize("F0	41	00	00	00	24	12	03	00	00	39	00 00 44	F7"), new MsgInputDynaAttack(0, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("dyn1-att-02", normalize("F0	41	00	00	00	24	12	03	00	00	39	3E 40 46	F7"), new MsgInputDynaAttack(0, 0, 1.0f)),  // 8000

			// hold 
			new MessagingSysexAssociation("dyn1-hld-01", normalize("F0	41	00	00	00	24	12	03	00	00	3D	00 00 40	F7"), new MsgInputDynaHold(0, 0, 0.0f)),  //     0
			new MessagingSysexAssociation("dyn1-hld-02", normalize("F0	41	00	00	00	24	12	03	00	00	3D	3E 40 42	F7"), new MsgInputDynaHold(0, 0, 1.0f)),  //  8000

			// decay (named release by roland)
			new MessagingSysexAssociation("dyn1-dcy-01", normalize("F0	41	00	00	00	24	12	03	00	00	3B	00 00 42	F7"), new MsgInputDynaDecayRelease(0, 0, 0.0f)),  //     0
			new MessagingSysexAssociation("dyn1-dcy-02", normalize("F0	41	00	00	00	24	12	03	00	00	3B	3E 40 44	F7"), new MsgInputDynaDecayRelease(0, 0, 1.0f)),  //  8000

			/* COMPRESSOR */ 
			// threshold 
			new MessagingSysexAssociation("dyn2-thr-01", normalize("F0	41	00	00	00	24	12	03	00	00	42	7C 70 4F	F7"), new MsgInputDynaThreshold(1, 0, 0.0f)),  //  -400
			new MessagingSysexAssociation("dyn2-thr-02", normalize("F0	41	00	00	00	24	12	03	00	00	42	00 00 3B	F7"), new MsgInputDynaThreshold(1, 0, 1.0f)),  //     0

			// ratio 
			new MessagingSysexAssociation("dyn2-rto-01", normalize("F0	41	00	00	00	24	12	03	00	00	44	00 39	F7"), new MsgInputDynaRatio(1, 0, 0.0f)),  //  0
			new MessagingSysexAssociation("dyn2-rto-02", normalize("F0	41	00	00	00	24	12	03	00	00	44	0D 2C	F7"), new MsgInputDynaRatio(1, 0, 1.0f)),  //  13

			// gain 
			new MessagingSysexAssociation("dyn2-gn-01", normalize("F0	41	00	00	00	24	12	03	00	00	4A	7C 70 47	F7"), new MsgInputDynaGain(1, 0, 0.0f)),  // -400
			new MessagingSysexAssociation("dyn2-gn-02", normalize("F0	41	00	00	00	24	12	03	00	00	4A	03 10 20	F7"), new MsgInputDynaGain(1, 0, 1.0f)),  //  400

			// attack 
			new MessagingSysexAssociation("dyn2-att-01", normalize("F0	41	00	00	00	24	12	03	00	00	46	00 00 37	F7"), new MsgInputDynaAttack(1, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("dyn2-att-02", normalize("F0	41	00	00	00	24	12	03	00	00	46	3E 40 39	F7"), new MsgInputDynaAttack(1, 0, 1.0f)),  // 8000

			// release 
			new MessagingSysexAssociation("dyn2-rls-01", normalize("F0	41	00	00	00	24	12	03	00	00	48	00 00 35	F7"), new MsgInputDynaDecayRelease(1, 0, 0.0f)),  //     0
			new MessagingSysexAssociation("dyn2-rls-02", normalize("F0	41	00	00	00	24	12	03	00	00	48	3E 40 37	F7"), new MsgInputDynaDecayRelease(1, 0, 1.0f)),  //  8000

			// knee 
			new MessagingSysexAssociation("dyn2-kne-01", normalize("F0	41	00	00	00	24	12	03	00	00	45	00 38	F7"), new MsgInputDynaKnee(1, 0, 0.0f)),  //   0
			new MessagingSysexAssociation("dyn2-kne-02", normalize("F0	41	00	00	00	24	12	03	00	00	45	09 2F	F7"), new MsgInputDynaKnee(1, 0, 1.0f)),  //   9


			/* AUX */
			new MessagingSysexAssociation("aux1-send-01", normalize("F0	41	00	00	00	24	12	03	00	01	02	40 00 3A	F7"), new MsgAuxSendChanged(0, 0, 0.0f)),  //  -8192 (-Inf)
			new MessagingSysexAssociation("aux1-send-02", normalize("F0	41	00	00	00	24	12	03	00	01	02	00 64 16	F7"), new MsgAuxSendChanged(0, 0, 1.0f)),  //    100

			new MessagingSysexAssociation("aux47-send-01", normalize("F0	41	00	00	00	24	12	03	2F	01	02	40 00 0B	F7"), new MsgAuxSendChanged(47, 0, 0.0f)),  //  -8192 (-Inf)
			new MessagingSysexAssociation("aux47-send-02", normalize("F0	41	00	00	00	24	12	03	2F	01	02	00 64 67	F7"), new MsgAuxSendChanged(47, 0, 1.0f)),  //    100

			/* DCA */
			new MessagingSysexAssociation("dca-master-01", normalize("F0	41	00	00	00	24	12	09 00 00 0A	40 00 2D	F7"), new MsgDcaLevelChanged(0, 0.0f)),  //  -8192 (-Inf)
			new MessagingSysexAssociation("dca-master-02", normalize("F0	41	00	00	00	24	12	09 00 00 0A	00 64 09	F7"), new MsgDcaLevelChanged(0, 1.0f)),  //    100


			/* OUTPUTS */
			// master out
			new MessagingSysexAssociation("master-out-01", normalize("F0	41	00	00	00	24	12	06	00	00	0E	40 00 2C	F7"), new MsgMasterLevelChanged(0.0f)),  //  -8192 (-Inf)
			new MessagingSysexAssociation("master-out-02", normalize("F0	41	00	00	00	24	12	06	00	00	0E	00 64 08	F7"), new MsgMasterLevelChanged(1.0f)),  //    100

			// aux outs
			new MessagingSysexAssociation("aux-master-out-01", normalize("F0	41	00	00	00	24	12	05	00	00	0E	40 00 2D	F7"), new MsgAuxMasterLevelChanged(0, 0.0f)),  //  -8192 (-Inf)
			new MessagingSysexAssociation("aux-master-out-02", normalize("F0	41	00	00	00	24	12	05	00	00	0E	00 64 09	F7"), new MsgAuxMasterLevelChanged(0, 1.0f)),  //    100

			/* GRAPHICAL EQUALIZER */
			// band level
			new MessagingSysexAssociation("geq-band-01", normalize("F0	41	00	00	00	24	12	0B	10	00	22	7E 6A 5B	F7"), new MsgGeqBandLevelChanged((short) 0, 0, false, -1.0f)),  //  -150
			new MessagingSysexAssociation("geq-band-02", normalize("F0	41	00	00	00	24	12	0B	10	00	22	00 00 43	F7"), new MsgGeqBandLevelChanged((short) 0, 0, false, 0.0f)),   //     0
			new MessagingSysexAssociation("geq-band-03", normalize("F0	41	00	00	00	24	12	0B	10	00	22	01 16 2C	F7"), new MsgGeqBandLevelChanged((short) 0, 0, false, 1.0f)),   //   150

	};


}
