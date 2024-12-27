import React from 'react';
import { Helmet } from 'react-helmet';

import { Messages } from 'primereact/messages';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { useTracked } from './../../Store';

let messages;

const Setting = () => {

  const [state, setState] = useTracked();

  return (
    <div>
      <Helmet title="Settings" />

      <div className="p-grid p-nogutter">
        <div className="p-col-12">
          <div className="p-fluid">
            <Messages ref={(el) => messages = el} />
          </div>
        </div>
      </div>

      <div className="p-grid">

        <div className="p-col-12 p-md-6">
          <Card className="rounded-border">
            <div>
              <div className="p-card-title p-grid p-nogutter p-justify-between">Interface Setting</div>
              <div className="p-card-subtitle">Below are the current setup for this UI.</div>
            </div>
            <br />
            <div className="p-grid p-nogutter p-justify-between">
              <h3 className="color-title p-col-4">
                Menu Color:
                </h3>
              <h3 className="color-highlight p-col-4">
                {state.layoutColorMode === 'dark' ? 'Dark' : 'Light'}
              </h3>
              <h3>
                <Button label="Toggle" icon="pi pi-refresh"
                  className="p-button-rounded p-button-raised p-button-secondary"
                  type="button"
                  onClick={(e) => {
                    state.layoutColorMode === 'dark' ? setState(prev => ({ ...prev, layoutColorMode: 'light' })) : setState(prev => ({ ...prev, layoutColorMode: 'dark' }))
                  }} />
              </h3>
            </div>
            <div className="p-grid p-nogutter p-justify-between">
              <h3 className="color-title p-col-4">
                Menu Mode:
                </h3>
              <h3 className="color-highlight p-col-4">
                {state.layoutMode === 'static' ? 'Static' : 'Overlay'}
              </h3>
              <h3>
                <Button label="Toggle" icon="pi pi-refresh"
                  className="p-button-rounded p-button-raised p-button-secondary"
                  type="button"
                  onClick={(e) => {
                    state.layoutMode === 'static' ? setState(prev => ({ ...prev, layoutMode: 'overlay' })) : setState(prev => ({ ...prev, layoutMode: 'static' }))
                  }} />
              </h3>
            </div>
          </Card>
        </div>

      </div>
    </div>

  )
}

export default React.memo(Setting);
